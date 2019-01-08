package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.utils.NukkitPlayer

abstract class GameInstance(val room: GameConfig) {
    val serialNumber = room.used++
    val players: HashMap<String, NukkitPlayer> = hashMapOf()
    var numberOfPlayers: Int = 0

    open fun join(player: NukkitPlayer) {//玩家进入进行的操作，请在玩家真的进入房间后进行super.join(player)来进行一些可以避免重复的代码信息
        players[player.name] = player
        numberOfPlayers++
        player.gameInfo = arrayOf(room.type, room.id, serialNumber)
    }

    abstract fun quit(player: NukkitPlayer)//玩家退出进行的操作，请在玩家真的进入房间后进行super.quit(player)来进行一些可以避免重复的代码信息

    abstract fun start()//游戏开始时进行的操作

    abstract fun stop()//游戏结束时的操作

    open fun reward() {//奖励 默认空

    }
}