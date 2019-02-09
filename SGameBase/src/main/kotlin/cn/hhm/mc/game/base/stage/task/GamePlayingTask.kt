package cn.hhm.mc.game.base.stage.task

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.utils.Games
import cn.nukkit.scheduler.PluginTask

open class GamePlayingTask(val instance: GameInstance) : PluginTask<SGameBase>(SGameBase.instance) {
    var tick = 0
    val type: Games = instance.room.type

    override fun onRun(t: Int) {
        this.tick++
        instance.gamingTick(tick)
        if (this.checkFinished()) {
            instance.onFinished()
        }
    }

    open fun checkFinished(): Boolean {
        return this.tick >= instance.room.gamingTime
    }
}