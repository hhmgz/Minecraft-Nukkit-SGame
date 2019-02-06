package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.stage.task.GamePlayingTask
import cn.hhm.mc.game.base.stage.task.GameWaitTask
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.hhm.mc.game.base.utils.config.Config
import cn.nukkit.Server
import cn.nukkit.level.Location

abstract class GameRoom(val type: Games, val id: String) {
    val waitingInstance: HashMap<Long, GameInstance> = hashMapOf()
    val runningInstance: HashMap<Long, GameInstance> = hashMapOf()
    var used: Long = 0
    var config: Config? = null
    var waitTaskClass: Class<out GameWaitTask> = GameWaitTask::class.java
    var gamingTaskClass: Class<out GamePlayingTask> = GamePlayingTask::class.java

    var waitTime: Int = 30
    var fastWaitTime: Int = 5
    var minOfPlayers: Int = 18
    var maxOfPlayers: Int = 30
    var waitLocation: Location = Server.getInstance().defaultLevel.safeSpawn.location
    var stopLocation: Location = Server.getInstance().defaultLevel.safeSpawn.location

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
            runningInstance[newInstance.serialNumber] = newInstance
            if (newInstance.stage == StageMode.WAITING) {
                waitingInstance[newInstance.serialNumber] = newInstance
            }
        } else {
            val s = waitingInstance.values.first()
            s.join(player)
            if (s.stage != StageMode.WAITING) {
                waitingInstance.remove(s.serialNumber, s)
            }
        }
    }

    abstract fun save()

    abstract fun configure(data: MutableMap<String,Any>)

    fun getWaitTask(instance: GameInstance) = waitTaskClass.getConstructor(GameInstance::class.java).newInstance(instance)

    fun getGamingTask(instance: GameInstance) = gamingTaskClass.getConstructor(GameInstance::class.java).newInstance(instance)

    companion object {
        fun loadFromFile(room: GameRoom): MutableMap<String, Any> {
            val conf = Config(SGameBase.gameModules[room.type]!!.absolutePath+"/rooms/"+room.id+".yml")
            room.config = conf
            return conf.getAll()
        }
    }
}