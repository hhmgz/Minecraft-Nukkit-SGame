package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.data.GameTipData
import cn.hhm.mc.game.base.stage.instance.CanDeathGameInstance
import cn.hhm.mc.game.base.utils.*

abstract class GameInstance(val room: GameRoom) {
    val serialNumber = room.used++
    val name = room.type.codeName + "_" + room.id + "_" + serialNumber
    val allPlayers: HashMap<String, NukkitPlayer> = hashMapOf()
    val playingPlayers: HashSet<String> = hashSetOf()
    val watchers: HashSet<String> = hashSetOf()
    var numberOfPlayers: Int = 0
    var stage: StageMode = StageMode.PRE_START

    open fun join(player: NukkitPlayer) {//玩家进入进行的操作，请在玩家真的进入房间后进行super.join(player)来进行一些可以避免重复的代码信息
        this.addPlayer(player)
        player.sendMessage("game.join.toEntrant" gTranslate arrayOf(name))
        this.broadcast(BroadcastType.MESSAGE, "game.join.toOthers" gTranslate arrayOf(player.name, numberOfPlayers, room.maxOfPlayers), arrayOf(player.name))
    }

    open fun quit(player: NukkitPlayer) {//玩家退出进行的操作，请在玩家真的进入房间后进行super.quit(player)来进行一些可以避免重复的代码信息
        this.delPlayer(player)
        player.sendMessage("game.quit.toEntrant" gTranslate arrayOf(name))
        this.broadcast(BroadcastType.MESSAGE, "game.quit.toOthers" gTranslate arrayOf(player.name, numberOfPlayers, room.maxOfPlayers), arrayOf(player.name))

    }

    open fun addPlayer(player: NukkitPlayer) {
        allPlayers[player.name] = player
        numberOfPlayers++
        player.gameInfo = arrayOf(room.type, room.id, serialNumber)
    }

    open fun delPlayer(player: NukkitPlayer) {
        allPlayers.remove(player.name)
        player.gameInfo = arrayOf()
        numberOfPlayers--
    }

    abstract fun start()//游戏开始时进行的操作

    abstract fun stop()//游戏结束时的操作

    abstract fun waitTick()

    open fun kickAllPlayers(reason: String, cause: String) {

    }

    open fun kick(name: String, reason: String, cause: String) {

    }

    open fun reward() {//奖励 默认空

    }

    open fun checkNumberOfPlayers() {
        if (numberOfPlayers <= 0) {
            this.kickAllPlayers("人数过少,系统自动关闭实例", "系统")
            this.close()
        }
    }

    infix fun String.gTranslate(params: Array<out Any>) = GameTipData.tipData[room.type]!!.translate(this, params)

    open fun broadcast(type: BroadcastType, msg: String, except: Array<String>, vararg range: BroadcastRange = arrayOf(BroadcastRange.ALL)) {
        val flag = this is CanDeathGameInstance && range.isNeedTransmit()
        if (!range.isOverlap()) {
            range.forEach { r ->
                when (type) {
                    BroadcastType.MESSAGE -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) }.forEach { _, p -> p.sendMessage(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendMessage(msg) }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendMessage(msg) }
                            else -> {
                            }
                        }
                    }
                    BroadcastType.TIP -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) }.forEach { _, p -> p.sendTip(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendTip(msg) }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendTip(msg) }
                            else -> {
                            }
                        }
                    }
                    BroadcastType.POP -> {
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) }.forEach { _, p -> p.sendPopup(msg) }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendPopup(msg) }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendPopup(msg) }
                            else -> {
                            }
                        }
                    }
                }
            }
            if (flag) (this as CanDeathGameInstance).broadcast0(type, msg, except, range, null, false)
        } else {
            val sent = hashSetOf<String>()
            when (type) {
                BroadcastType.MESSAGE -> {
                    range.forEach { r ->
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) && !sent.contains(it.key) }.forEach { _, p ->
                                    p.sendMessage(msg)
                                    sent.add(p.name)

                            }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->
                                allPlayers[s]?.sendMessage(msg)
                                sent.add(s)
                            }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->
                                allPlayers[s]?.sendMessage(msg)
                                sent.add(s)
                            }
                            else -> return
                        }
                    }
                }
                BroadcastType.TIP -> {
                    range.forEach { r ->
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) && !sent.contains(it.key) }.forEach { _, p ->
                                    p.sendTip(msg)
                                    sent.add(p.name)

                            }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->
                                    allPlayers[s]?.sendTip(msg)
                                    sent.add(s)

                            }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->

                                    allPlayers[s]?.sendTip(msg)
                                    sent.add(s)

                            }
                            else -> {
                            }
                        }
                    }
                }
                BroadcastType.POP -> {
                    range.forEach { r ->
                        when (r) {
                            BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) && !sent.contains(it.key) }.forEach { _, p ->
                                p.sendMessage(msg)
                                sent.add(p.name)
                            }
                            BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->

                                    allPlayers[s]?.sendPopup(msg)
                                    sent.add(s)
                            }
                            BroadcastRange.WATCH -> watchers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->

                                    allPlayers[s]?.sendPopup(msg)
                                    sent.add(s)

                            }
                            else -> {
                            }
                        }
                    }
                }
            }
            if (flag) (this as CanDeathGameInstance).broadcast0(type, msg, except, range, sent, true)
        }
        return
    }

    open fun sendTitle(msg: String, subMessage: String = "", fadeIn: Int = 10, stay: Int = 30, fadeOut: Int = 10, except: Array<String>, vararg range: BroadcastRange = arrayOf(BroadcastRange.ALL)) {
        val flag = this is CanDeathGameInstance && range.isNeedTransmit()
        if (!range.isOverlap()) {
            when (range) {
                BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) }.forEach { _, p -> p.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                BroadcastRange.WATCH -> watchers.filter { !except.contains(it) }.forEach { s -> allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut) }
                else -> {
                }
            }
            if (flag) (this as CanDeathGameInstance).sendTitle0(msg, subMessage, fadeIn, stay, fadeOut, except, range, null, false)
        } else {
            val sent = hashSetOf<String>()
            when (range) {
                BroadcastRange.ALL -> allPlayers.filter { !except.contains(it.key) && !sent.contains(it.key) }.forEach { _, p ->
                    if (!sent.contains(p.name)) {
                        p.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(p.name)
                    }
                }
                BroadcastRange.PLAYING -> playingPlayers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->
                    if (!sent.contains(s)) {
                        allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(s)
                    }
                }
                BroadcastRange.WATCH -> watchers.filter { !except.contains(it) && !sent.contains(it) }.forEach { s ->
                    if (!sent.contains(s)) {
                        allPlayers[s]?.sendTitle(msg, subMessage, fadeIn, stay, fadeOut)
                        sent.add(s)
                    }
                }
                else -> {
                }
            }
            if (flag) (this as CanDeathGameInstance).sendTitle0(msg, subMessage, fadeIn, stay, fadeOut, except, range, sent, true)
        }
    }

    open fun close() {

    }
}

fun String.gTranslate(type: Games, vararg params: Any) = GameTipData.tipData[type]!!.translate(this, params)