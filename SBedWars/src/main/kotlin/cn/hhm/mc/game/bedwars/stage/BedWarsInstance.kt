package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.data.translate
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.base.stage.StageMode
import cn.hhm.mc.game.base.stage.instance.CanDeathGameInstance
import cn.hhm.mc.game.base.utils.BroadcastType
import cn.hhm.mc.game.base.utils.NukkitUtils
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.hhm.mc.game.bedwars.config.MasterConfig
import cn.hhm.mc.game.bedwars.entity.EntityEnderCrystal
import cn.hhm.mc.game.bedwars.stage.mineral.BedWarsMineralInstance
import cn.hhm.mc.game.bedwars.stage.team.BedWarsTeamInstance
import cn.nukkit.entity.Entity
import cn.nukkit.event.player.PlayerDeathEvent
import cn.nukkit.item.Item
import cn.nukkit.level.Sound
import money.Money
import java.util.*
import kotlin.math.roundToInt

/**
 * 经验增加: 胜利:x1.5 VIP:x2
 * 金币增加: 同上
 * */

class BedWarsInstance(room: BedWarsRoom) : CanDeathGameInstance(room) {
    val realRoom
        get() = room as BedWarsRoom
    val teamData
        get() = realRoom.teamData
    val teamInstance = hashMapOf<Int, BedWarsTeamInstance>().apply { teamData.forEach { t, u -> this[t] = u.createInstance() } }
    val mineralInstance = BedWarsMineralInstance(this, realRoom.mineral)
    val revivePlayers: HashMap<NukkitPlayer, Int> = hashMapOf()
    val enderCrystals: HashMap<Long, Int> = hashMapOf()

    fun asWatching(player: NukkitPlayer) {
        alivePlayers.remove(player.name)
        deathPlayers.remove(player.name)
        playingPlayers.remove(player.name)
        watchers.add(player.name)
        player.setGamemode(3)
        val gi = player.gameInfo!! as BedWarsPlayerGamingInformation
        val ti = gi.team!!
        ti.gamingPlayers.remove(player.name)
        player.removeAllEffects()
        player.setOnFire(0)
        player.teleport(realRoom.watchingLocation)
    }

    fun onRevive(player: NukkitPlayer) {
        player.setGamemode(0)
        player.teleport((player.gameInfo as BedWarsPlayerGamingInformation).team!!.team.spawnLocation)
        player.maxHealth = 20
        player.health = 20f
        player.sendTitle(TITLE, "§6你复活了!")
    }

    fun onDie(event: PlayerDeathEvent, player: NukkitPlayer) {
        event.drops = arrayOf<Item>()
        event.isCancelled = true
        event.keepInventory = false
        val killer = if (event.entity.killer != null) {
            event.entity.killer
        } else {
            this.onDieByOthers(player, event)
            return
        }
        if (killer is NukkitPlayer) {
            this.onDieByPlayer(killer, player, event)
        } else {
            this.onDieByEntity(killer, player, event)
        }
    }

    private fun onDie0(event: PlayerDeathEvent, player: NukkitPlayer) {
        val gi = player.gameInfo!! as BedWarsPlayerGamingInformation
        val ti = gi.team!!
        gi.death++
        if (ti.isBroken) {
            player.sendMessage("bw.game.die.beWatching" translate arrayOf())
            this.asWatching(player)
        } else {
            revivePlayers[player] = MasterConfig.reviveTime
            player.setGamemode(3)
            player.removeAllEffects()
            player.setOnFire(0)
            player.teleport(realRoom.watchingLocation)
        }
    }

    private fun onDieByOthers(player: NukkitPlayer, event: PlayerDeathEvent) {
        this.sendTitle("§6玩家" + this.name + "死了", "", arrayOf())
        this.onDie0(event, player)
    }

    private fun onDieByEntity(killer: Entity, player: NukkitPlayer, event: PlayerDeathEvent) {
        this.sendTitle("§6玩家" + name + "被" + killer.nameTag + "杀死了", "", arrayOf())
        killer.getLevel().addSound(killer, Sound.RANDOM_ANVIL_BREAK)
        this.onDie0(event, player)
    }

    private fun onDieByPlayer(killer: NukkitPlayer, player: NukkitPlayer, event: PlayerDeathEvent) {
        val gi = killer.gameInfo as BedWarsPlayerGamingInformation
        gi.kill++
        this.sendTitle("§6玩家" + name + "被" + killer.displayName + "杀死了", "", arrayOf())
        killer.getLevel().addSound(killer, Sound.RANDOM_ANVIL_BREAK)
        this.onDie0(event, player)
    }

    override fun addPlayer(player: NukkitPlayer) {
        player.gameInfo = BedWarsPlayerGamingInformation(realRoom, this)
        super.addPlayer(player)
    }

    override fun delPlayer(player: NukkitPlayer) {
        player.gameInfo = null
        super.delPlayer(player)
    }

    override fun start() {
        this.distributionTeam()
        teamInstance.forEach { _, u ->
            u.players.forEach {
                val p = allPlayers[it]!!
                p.setGamemode(0)
                p.maxHealth = 20
                p.health = 20f
                p.removeAllEffects()
                p.teleport(u.team.spawnLocation)
            }
        }
        teamInstance.forEach { _, u ->
            u.spawnShop(this)
        }
        this.initEnderCrystal()
        super.start()
    }

    override fun stop() {
        this.despawnEnderCrystal()
        stage = StageMode.STOP
        realRoom.loadPosition()
    }

    override fun waitTick(tick: Int) {

    }

    override fun gamingTick(tick: Int) {
        this.checkTeam()
        mineralInstance.tick(tick)
    }

    fun win(team: BedWarsTeamInstance) {
        team.players.forEach {
            val player = allPlayers[it]!!
            val exp = MasterConfig.singleExperienceIncreasesQuantity * 2 * (if (player.gameData.isVip) 2 else 1)
            val money = (MasterConfig.singleMoneyIncreasesQuantity * 2 * (if (player.gameData.isVip) 2 else 1)).toFloat()
            player.sendTitle("§6你所在的队伍:${team.team.name}§6获得了最终的胜利!", if (player.gameData.isVip) "你是§cVIP,§b获得了 §f$exp §b点经验,§f$money §e枚金币" else "你§b获得了 §f$exp §b点经验,§f$money §e枚金币")
            Money.getInstance().addMoney(it, money)
            player.gameData.addExp(exp)
        }
        teamInstance.filter { it.value != team }.forEach { t, u ->
            u.players.forEach {
                val player = allPlayers[it]!!
                val exp = MasterConfig.singleExperienceIncreasesQuantity * (if (player.gameData.isVip) 2 else 1)
                val money = (MasterConfig.singleMoneyIncreasesQuantity * (if (player.gameData.isVip) 2 else 1)).toFloat()
                player.sendTitle("$TITLE §6队伍:${team.team.name}§6获得了最终的胜利!", if (player.gameData.isVip) "你是§cVIP,§b获得了 §f$exp §b点经验,§f$money §e枚金币" else "你§b获得了 §f$exp §b点经验,§f$money §e枚金币")
                Money.getInstance().addMoney(it, money)
                player.gameData.addExp(exp)
            }
        }
        this.stop()
    }

    fun draw() {
        teamInstance.forEach { t, u ->
            u.players.forEach {
                val player = allPlayers[it]!!
                val exp = (MasterConfig.singleExperienceIncreasesQuantity * 1.5 * (if (player.gameData.isVip) 2 else 1)).roundToInt()
                val money = (MasterConfig.singleMoneyIncreasesQuantity * 1.5 * (if (player.gameData.isVip) 2 else 1)).toFloat()
                player.sendTitle("$TITLE 平局:§c游戏结束", if (player.gameData.isVip) "你是§cVIP,§b获得了 §f$exp §b点经验,§f$money §e枚金币" else "你§b获得了 §f$exp §b点经验,§f$money §e枚金币")
                Money.getInstance().addMoney(it, money)
                player.gameData.addExp(exp)
            }
        }
        this.stop()
    }

    fun chooseTeam(id: String, team: Int, system: Boolean) {
        val ti = this.teamInstance[team]!!
        val p = allPlayers[id]!!
        if (system) {
            (p.gameInfo as BedWarsPlayerGamingInformation).team = ti
            ti.players.add(p.name)
            if (alivePlayers.contains(id)) {
                ti.gamingPlayers.add(id)
            }
            p.sendMessage("bw.game.team.choose.auto" translate arrayOf(this.teamData[team]!!.name))
        } else {
            if (ti.players.size >= ti.team.maxOfPlayers) {
                p.sendMessage("bw.game.team.choose.max" translate arrayOf(this.teamData[team]!!.name))
                return
            }
            if (ti.players.size - 2 >= this.getHasTheMinPlayers()) {
                p.sendMessage("bw.game.team.choose.tooMuch" translate arrayOf(this.teamData[team]!!.name))
                return
            }
            (p.gameInfo as BedWarsPlayerGamingInformation).team = ti
            ti.players.add(p.name)
            if (alivePlayers.contains(id)) {
                ti.gamingPlayers.add(id)
            }
            p.sendMessage("bw.game.team.choose.manual" translate arrayOf(this.teamData[team]!!.name))
        }
    }

    private fun getNotHasTeamPlayers() = this.alivePlayers.filter { (allPlayers[it]!!.gameInfo as BedWarsPlayerGamingInformation).team == null }

    private fun distributionTeam() {
        this.getNotHasTeamPlayers().forEach {
            val id = this.getHasTheMinPlayersId()
            this.chooseTeam(it, id, true)
        }
    }

    private fun getHasTheMinPlayers(): Int {
        var num = 10000
        this.teamInstance.forEach { t, u ->
            if (u.players.size < num) {
                num = u.players.size
            }
        }
        return num
    }

    private fun getHasTheMinPlayersId(): Int {
        var teamID = -1
        var num = 10000
        this.teamInstance.forEach { t, u ->
            if (u.players.size < num) {
                num = u.players.size
                teamID = t
            }
        }
        return teamID
    }

    private fun checkTeam() {
        var i = 0
        var team: BedWarsTeamInstance? = null
        teamInstance.forEach { _, data ->
            if (data.gamingPlayers.size == 0) {
                i++
            } else {
                team = data
            }
        }
        if (i == teamData.size - 1) {
            if (team != null) {
                this.win(team!!)
                this.gamingTask?.cancel()
            }
        }
    }

    override fun onFinished() {
        this.draw()
    }

    private fun initEnderCrystal() {
        teamInstance.forEach { t, u ->
            val pos = u.team.coreLocation
            val ec = EntityEnderCrystal(pos)
            allPlayers.values.forEach {
                ec.spawnTo(it)
            }
            u.enderCrystal = ec
            enderCrystals[ec.eid] = t
            NukkitUtils.addFloatingText("bw_${name}_ec_$t", "§3队伍: " + u.team.name + "  §e能量水晶", "§a██████████ 100%", pos.clone().add(0.0, 4.0))
        }
    }

    private fun despawnEnderCrystal() {
        teamInstance.forEach { t, u ->
            allPlayers.values.forEach {
                u.enderCrystal.despawn(it)
            }
            u.enderCrystal.close()
            NukkitUtils.removeFloatingText("bw_${name}_ec_$t")
        }
    }

    fun attackCrystal(player: NukkitPlayer, item: Item, eid: Long): Boolean {
        if (!enderCrystals.containsKey(eid)) return false
        println(player.name + " Attack " + eid)
        val gi = player.gameInfo as? BedWarsPlayerGamingInformation ?: return false
        val tTeam = teamInstance[enderCrystals[eid] ?: return false] ?: return false
        val crystal: EntityEnderCrystal = tTeam.enderCrystal
        val pTeam = gi.team ?: return false
        if (tTeam == pTeam) {
            player.sendMessage(TITLE + "你不能破坏你自己家里的核心!")
            return true
        }
        val damage = item.attackDamage.toFloat()
        println("damage: $damage")
        crystal.health -= damage.toInt()
        if (crystal.health <= 0) {
            allPlayers.values.forEach(crystal::despawn)
            tTeam.isBroken = true
            NukkitUtils.removeFloatingText("bw_${name}_ec_" + tTeam.team.id)
            tTeam.enderCrystal.close()
            gi.breakCore++
            this.broadcast(BroadcastType.MESSAGE, "队伍:" + tTeam.team.name + "的核心被破坏,此队伍将不可重生", arrayOf())
            this.sendTitle(TITLE, "队伍:" + tTeam.team.name + "的核心被破坏,此队伍将不可重生", arrayOf())
        } else {
            this.updateCrystalFloatingText(crystal, tTeam)
        }
        return true
    }

    private fun updateCrystalFloatingText(ec: EntityEnderCrystal, team: BedWarsTeamInstance) {
        val health = StringBuilder()
        health.append("§a")
        var i = 10
        while (i <= 100) {
            if (ec.health >= i) {
                health.append("█")
            }
            i += 10
        }
        health.append("   ").append(ec.health).append("%")
        println("${team.team.id} Update Floating Text :" + health.toString() + " ,Now Health:" + ec.health)
        NukkitUtils.setFloatingText("bw_${name}_ec_" + team.team.id, "§3队伍: " + team.team.name + "  §e能量水晶", health.toString())
    }
}