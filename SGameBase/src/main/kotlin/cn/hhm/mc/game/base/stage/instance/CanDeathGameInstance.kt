package cn.hhm.mc.game.base.stage.instance

import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.BroadcastRange
import cn.hhm.mc.game.base.utils.BroadcastType

abstract class CanDeathGameInstance(room: GameRoom) : GameInstance(room) {
    val alivePlayers: HashSet<String> = hashSetOf()
    val deathPlayers: HashSet<String> = hashSetOf()

    fun broadcast0(type: BroadcastType, msg: String, range: Array<out BroadcastRange>, sent: HashSet<String>?, overlap: Boolean) {

    }
}