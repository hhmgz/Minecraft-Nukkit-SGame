package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.hhm.mc.game.base.utils.config.Config

abstract class GameRoom(val type: Games, val id: String) {
    val waitingInstance: ArrayList<GameInstance> = arrayListOf()
    val runningInstance: HashMap<Long, GameInstance> = hashMapOf()
    var used: Long = 0
    var config: Config? = null

    var waitTime: Int = 30
    var fastWaitTime: Int = 5

    open fun load(){
        this.configure(GameRoom.loadFromFile(this))
    }

    open fun reload(){
        this.load()
    }

    @Synchronized
    open fun join(player: NukkitPlayer) {
        if (waitingInstance.isEmpty()) {
            val newInstance: GameInstance = type.instance.gameInstance.getConstructor(GameRoom::class.java).newInstance(this)
            newInstance.join(player)
            if (newInstance.stage == StageMode.PRE_START) {
                waitingInstance.add(newInstance)
            }
        } else {
            waitingInstance[0].join(player)
        }
    }

    abstract fun save()

    abstract fun configure(data: MutableMap<String,Any>)

    companion object {
        fun loadFromFile(room: GameRoom): MutableMap<String, Any> {
            val conf = Config(SGameBase.gameModules[room.type]!!.absolutePath+"/rooms/"+room.id+".yml")
            room.config = conf
            return conf.getAll()
        }
    }
}