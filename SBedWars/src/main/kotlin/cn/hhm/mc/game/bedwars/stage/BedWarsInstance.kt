package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.data.translate
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.base.stage.instance.CanDeathGameInstance
import cn.hhm.mc.game.bedwars.stage.mineral.BedWarsMineralInstance
import cn.hhm.mc.game.bedwars.stage.team.BedWarsTeamInstance

class BedWarsInstance(room: BedWarsRoom) : CanDeathGameInstance(room) {
    val realRoom
        get() = room as BedWarsRoom
    val teamData
        get() = realRoom.teamData
    val teamInstance = hashMapOf<Int, BedWarsTeamInstance>().apply { teamData.forEach { t, u -> this[t] = BedWarsTeamInstance(u) } }
    val mineralInstance = BedWarsMineralInstance(this, realRoom.mineral)

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
    }

    override fun stop() {

    }

    override fun waitTick(tick: Int) {

    }

    override fun gamingTick(tick: Int) {
        this.checkTeam()
        mineralInstance.tick(tick)
    }

    fun win(team: BedWarsTeamInstance) {

        this.stop()
    }

    fun draw() {

        this.stop()
    }

    fun chooseTeam(id: String, team: Int, system: Boolean) {
        val ti = this.teamInstance[team]!!
        val p = allPlayers[id]!!
        if (system) {
            (p.gameInfo as BedWarsPlayerGamingInformation).team = ti
            ti.players.add(p.name)
            if (alivePlayers.contains(id)) {
                ti.alivePlayers.add(id)
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
                ti.alivePlayers.add(id)
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
            if (data.alivePlayers.size == 0) {
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
}