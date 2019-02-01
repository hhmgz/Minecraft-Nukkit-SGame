package cn.hhm.mc.game.base.stage.instance

import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.BroadcastRange

abstract class CanDeathGameInstance(room: GameRoom) : GameInstance(room) {
    val alivePlayers: HashSet<String> = hashSetOf()
    val deathPlayers: HashSet<String> = hashSetOf()
    override fun sendTitle(msg: String, subMessage: String, fadeIn: Int, stay: Int, fadeOut: Int, range: BroadcastRange): Boolean {
        if (super.sendTitle(msg, subMessage, fadeIn, stay, fadeOut, range)) {
            return true
        }
        if (range == BroadcastRange.ALIVE) {

        }
        return
    }
}