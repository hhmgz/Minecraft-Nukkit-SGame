package cn.hhm.mc.game.base.utils.scoreboard.scoreboard

import cn.hhm.mc.game.base.utils.scoreboard.packet.RemoveObjectivePacket
import cn.hhm.mc.game.base.utils.scoreboard.packet.SetDisplayObjectivePacket
import cn.nukkit.Player
import cn.nukkit.Server
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap

/**
 * @author CreeperFace
 */
open class FakeScoreboard : Scoreboard {

    private val players = Long2ObjectOpenHashMap<Player>()

    override lateinit var objective: DisplayObjective

    override fun update() {
        val pks = objective.objective.getChanges()

        if (pks.isNotEmpty()) {
            pks.forEach { pk ->
                Server.broadcastPacket(players.values, pk)
            }
        }

        if (objective.objective.needResend) {
            players.forEach { _, p ->
                despawnFrom(p)
                spawnTo(p)
            }
        }

        objective.objective.resetChanges()
    }

    override fun addPlayer(p: Player) {
        players[p.id] = p
        spawnTo(p)
    }

    override fun removePlayer(p: Player) {
        players.remove(p.id)
        despawnFrom(p)
    }

    override fun spawnTo(p: Player) {
        val pk = SetDisplayObjectivePacket(objective)

        p.dataPacket(pk)
        sendScores(p)
    }

    override fun despawnFrom(p: Player) {
        val pk = RemoveObjectivePacket(objective.objective.name)
        p.dataPacket(pk)
    }

    override fun sendScores(p: Player) {
        val pk = objective.objective.getScorePacket()

        pk?.let {
            p.dataPacket(it)
        }
    }
}