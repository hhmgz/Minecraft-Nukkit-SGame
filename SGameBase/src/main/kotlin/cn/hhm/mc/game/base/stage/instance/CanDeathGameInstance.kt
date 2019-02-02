package cn.hhm.mc.game.base.stage.instance

import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.BroadcastRange
import cn.hhm.mc.game.base.utils.BroadcastType

abstract class CanDeathGameInstance(room: GameRoom) : GameInstance(room) {
    val alivePlayers: HashSet<String> = hashSetOf()
    val deathPlayers: HashSet<String> = hashSetOf()

    fun broadcast0(type: BroadcastType, msg: String, range: Array<out BroadcastRange>, sent: HashSet<String>?, overlap: Boolean) {
        sent ?: return
        if (range.contains(BroadcastRange.ALIVE)) {
            alivePlayers.forEach { s ->
                when (type) {
                    BroadcastType.MESSAGE -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendMessage(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendMessage(msg)
                        }
                    }
                    BroadcastType.TIP -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendTip(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendTip(msg)
                        }
                    }
                    BroadcastType.POP -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendPopup(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendPopup(msg)
                        }
                    }
                }
            }
        }
        if (range.contains(BroadcastRange.DEATHWATCH)) {
            deathPlayers.forEach { s ->
                when (type) {
                    BroadcastType.MESSAGE -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendMessage(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendMessage(msg)
                        }
                    }
                    BroadcastType.TIP -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendTip(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendTip(msg)
                        }
                    }
                    BroadcastType.POP -> {
                        if (overlap) {
                            if (!sent.contains(s)) {
                                allPlayers[s]?.sendPopup(msg)
                                sent.add(s)
                            }
                        } else {
                            allPlayers[s]?.sendPopup(msg)
                        }
                    }
                }
            }
        }
    }

    fun sendTitle0(msg: String, subMessage: String = "", fadeIn: Int = 10, stay: Int = 30, fadeOut: Int = 10, range: Array<out BroadcastRange>, sent: HashSet<String>?, overlap: Boolean) {

    }
}