package cn.hhm.mc.game.bedwars.stage.task

import cn.hhm.mc.game.base.data.translate
import cn.hhm.mc.game.base.stage.task.GameWaitTask
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.stage.BedWarsInstance

class BedWarsWaitTask(instance: BedWarsInstance) : GameWaitTask(instance) {
    override fun sendCountDownMessage() {
        if (tick > 10) {
            instance.sendScoreboard(SBedWars.TITLE, ("bw.game.wait.scoreboard.begining" translate arrayOf(instance.name, tick)).split("%nl").toTypedArray(), arrayOf())
        } else if (tick in 1..10) {
            instance.sendTitle("game.wait.countDown.final" translate arrayOf(tick), type.mainTitle, arrayOf())
        }
    }
}