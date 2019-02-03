package cn.hhm.mc.game.base.stage.task

import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.utils.Games
import cn.nukkit.scheduler.AsyncTask

class GameWaitTask(val instance: GameInstance) : AsyncTask() {
    var tick = instance.room.waitTime
    val type: Games = instance.room.type

    override fun onRun() {
        this.tick--
        instance.waitTick()
    }
}