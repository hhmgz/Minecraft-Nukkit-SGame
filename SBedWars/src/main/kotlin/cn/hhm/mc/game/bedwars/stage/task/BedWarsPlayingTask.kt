package cn.hhm.mc.game.bedwars.stage.task

import cn.hhm.mc.game.base.stage.task.GamePlayingTask
import cn.hhm.mc.game.bedwars.stage.BedWarsInstance

class BedWarsPlayingTask(instance: BedWarsInstance) : GamePlayingTask(instance) {
    val realInstance
        get() = instance as BedWarsInstance
}