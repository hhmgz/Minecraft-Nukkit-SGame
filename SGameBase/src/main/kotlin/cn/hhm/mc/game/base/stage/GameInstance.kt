package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.stage.instance.CanDeathGameInstance
import cn.hhm.mc.game.base.utils.*

abstract class GameInstance(val room: GameRoom) {
    val serialNumber = room.used++
    val allPlayers: HashMap<String, NukkitPlayer> = hashMapOf()
    val playingPlayers: HashSet<String> = hashSetOf()
    val watchers: HashSet<String> = hashSetOf()
    var numberOfPlayers: Int = 0
    var stage: StageMode = StageMode.PRE_START

    open fun join(player: NukkitPlayer) {//玩家进入进行的操作，请在玩家真的进入房间后进行super.join(player)来进行一些可以避免重复的代码信息
        allPlayers[player.name] = player
        numberOfPlayers++
        player.gameInfo = arrayOf(room.type, room.id, serialNumber)
    }

    abstract fun quit(player: NukkitPlayer)//玩家退出进行的操作，请在玩家真的进入房间后进行super.quit(player)来进行一些可以避免重复的代码信息

    abstract fun start()//游戏开始时进行的操作

    abstract fun stop()//游戏结束时的操作

    abstract fun waitTick()

    open fun reward() {//奖励 默认空

    }

    open fun broadcast(type: BroadcastType, msg: String, vararg range: BroadcastRange = arrayOf(BroadcastRange.ALL)) {
        val flag = this is CanDeathGameInstance && range.isNeedTransmit()
        if (!range.isOverlap()) {
            range.forEach { r ->
                when (type) {
                    BroadcastType.MESSAGE -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p -> p.sendMessage(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s -> allPlayers[s]?.sendMessage(msg) }
                            BroadcastRange.WATCH -> watchers.forEach { s -> allPlayers[s]?.sendMessage(msg) }
                            else -> {
                            }
                        }
                    }
                    BroadcastType.TIP -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p -> p.sendTip(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s -> allPlayers[s]?.sendTip(msg) }
                            BroadcastRange.WATCH -> watchers.forEach { s -> allPlayers[s]?.sendTip(msg) }
                            else -> {
                            }
                        }
                    }
                    BroadcastType.POP -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p -> p.sendPopup(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s -> allPlayers[s]?.sendPopup(msg) }
                            BroadcastRange.WATCH -> watchers.forEach { s -> allPlayers[s]?.sendPopup(msg) }
                            else -> {
                            }
                        }
                    }
                }
            }
            if (flag) (this as CanDeathGameInstance).broadcast0(type, msg, range, null, false)
        } else {
            val sent = hashSetOf<String>()
            when (type) {
                BroadcastType.MESSAGE -> {
                    range.forEach {
                        when (it) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p ->
                                if (!sent.contains(p.name)) {
                                    p.sendMessage(msg)
                                    sent.add(p.name)
                                }
                            }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendMessage(msg)
                                    sent.add(s)
                                }
                            }
                            BroadcastRange.WATCH -> watchers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendMessage(msg)
                                    sent.add(s)
                                }
                            }
                            else -> return
                        }
                    }
                }
                BroadcastType.TIP -> {
                    range.forEach {
                        when (it) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p ->
                                if (!sent.contains(p.name)) {
                                    p.sendTip(msg)
                                    sent.add(p.name)
                                }
                            }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendTip(msg)
                                    sent.add(s)
                                }
                            }
                            BroadcastRange.WATCH -> watchers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendTip(msg)
                                    sent.add(s)
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
                BroadcastType.POP -> {
                    range.forEach {
                        when (it) {
                            BroadcastRange.ALL -> allPlayers.forEach { _, p ->
                                if (!sent.contains(p.name)) {
                                    p.sendMessage(msg)
                                    sent.add(p.name)
                                }
                            }
                            BroadcastRange.PLAYING -> playingPlayers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendPopup(msg)
                                    sent.add(s)
                                }
                            }
                            BroadcastRange.WATCH -> watchers.forEach { s ->
                                if (!sent.contains(s)) {
                                    allPlayers[s]?.sendPopup(msg)
                                    sent.add(s)
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
            if (flag) (this as CanDeathGameInstance).broadcast0(type, msg, range, sent, true)
        }
        return
    }

    open fun sendTitle(msg: String, subMessage: String = "", fadeIn: Int = 10, stay: Int = 30, fadeOut: Int = 10, vararg range: BroadcastRange = arrayOf(BroadcastRange.ALL)) {
        val flag = this is CanDeathGameInstance && range.isNeedTransmit()
        if (!range.isOverlap()) {
            when (range) {
                BroadcastRange.ALL -> allPlayers.forEach { _, p -> p.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                BroadcastRange.PLAYING -> playingPlayers.forEach { s -> allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                BroadcastRange.WATCH -> watchers.forEach { s -> allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                else -> {
                }
            }
            if (flag) (this as CanDeathGameInstance).sendTitle0(msg, subMessage, fadeIn, stay, fadeOut, range, null, false)
        } else {
            val sent = hashSetOf<String>()
            when (range) {
                BroadcastRange.ALL -> allPlayers.forEach { _, p ->
                    if (!sent.contains(p.name)) {
                        p.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(p.name)
                    }
                }
                BroadcastRange.PLAYING -> playingPlayers.forEach { s ->
                    if (!sent.contains(s)) {
                        allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(s)
                    }
                }
                BroadcastRange.WATCH -> watchers.forEach { s ->
                    if (!sent.contains(s)) {
                        allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(s)
                    }
                }
                else -> {
                }
            }
            if (flag) (this as CanDeathGameInstance).sendTitle0(msg, subMessage, fadeIn, stay, fadeOut, range, sent, true)
        }
    }
}