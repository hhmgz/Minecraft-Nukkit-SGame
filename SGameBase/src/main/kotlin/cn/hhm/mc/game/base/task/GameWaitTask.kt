package cn.hhm.mc.game.base.task

import cn.hhm.mc.game.base.stage.GameInstance
import cn.nukkit.scheduler.AsyncTask

class GameWaitTask(val instance: GameInstance) : AsyncTask() {
    var tick = instance.room.waitTime

    override fun onRun() {
        instance.waitTick()
    }
}