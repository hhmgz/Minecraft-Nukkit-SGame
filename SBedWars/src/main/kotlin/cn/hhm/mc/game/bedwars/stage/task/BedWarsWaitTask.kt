package cn.hhm.mc.game.bedwars.stage.task

import cn.hhm.mc.game.base.stage.gTranslate
import cn.hhm.mc.game.base.stage.task.GameWaitTask
import cn.hhm.mc.game.base.utils.BroadcastType
import cn.hhm.mc.game.bedwars.stage.BedWarsInstance

class BedWarsWaitTask(instance: BedWarsInstance) : GameWaitTask(instance) {
    override fun sendCountDownMessage() {
        if (tick > 10) {
            instance.broadcast(BroadcastType.MESSAGE, "game.wait.countDown.beginning".gTranslate(type, tick), arrayOf())
        } else if (tick in 1..10) {
            instance.sendTitle("game.wait.countDown.final".gTranslate(type, tick), type.mainTitle, arrayOf())
        }
    }
}