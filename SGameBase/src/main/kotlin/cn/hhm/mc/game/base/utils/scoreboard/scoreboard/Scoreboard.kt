package cn.hhm.mc.game.base.utils.scoreboard.scoreboard

import cn.nukkit.Player

/**
 * @author CreeperFace
 */
interface Scoreboard {

    var objective: DisplayObjective

    fun update()

    fun addPlayer(p: Player)

    fun removePlayer(p: Player)

    fun spawnTo(p: Player)

    fun despawnFrom(p: Player)

    fun sendScores(p: Player)
}