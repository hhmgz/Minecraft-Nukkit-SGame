package cn.hhm.mc.game.base.stage.task

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.data.translate
import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.utils.BroadcastType
import cn.hhm.mc.game.base.utils.Games
import cn.nukkit.scheduler.PluginTask

open class GameWaitTask(val instance: GameInstance) : PluginTask<SGameBase>(SGameBase.instance) {
    var tick = instance.room.waitTime
    val type: Games = instance.room.type

    override fun onRun(t: Int) {
        this.tick--
        this.sendCountDownMessage()
        instance.waitTick(tick)
        this.checkStart()
    }

    open fun checkStart() {
        if (this.tick <= 0) {
            instance.start()
            instance.waitTask = null
            this.cancel()
        }
    }

    open fun sendCountDownMessage() {
        if (tick > 10) {
            instance.broadcast(BroadcastType.MESSAGE, "base.game.wait.countDown.beginning" translate arrayOf(tick), arrayOf())
        } else if (tick in 1..10) {
            instance.sendTitle("base.game.wait.countDown.final" translate arrayOf(tick), type.mainTitle, arrayOf())
        }
    }
}