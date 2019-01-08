package cn.hhm.mc.game.base.stage

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.config.Config

abstract class GameConfig(val type: Games,val id: String) {
    var used: Long = 0
    var config: Config? = null
    open fun load(){
        this.configure(GameConfig.loadFromFile(this))
    }

    open fun reload(){
        this.load()
    }

    abstract fun save()

    abstract fun configure(data: MutableMap<String,Any>)

    companion object {
        fun loadFromFile(room: GameConfig) : MutableMap<String,Any>{
            val conf = Config(SGameBase.gameModules[room.type]!!.absolutePath+"/rooms/"+room.id+".yml")
            room.config = conf
            return conf.getAll()
        }
    }
}