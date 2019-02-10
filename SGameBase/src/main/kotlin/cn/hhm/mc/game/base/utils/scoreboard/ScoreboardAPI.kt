package cn.hhm.mc.game.base.utils.scoreboard

import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveCriteria
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveDisplaySlot
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveSortOrder
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.SimpleScoreboard

import java.util.*

/**
 * @author CreeperFace
 */
object ScoreboardAPI {
    private var criteria = ObjectiveCriteria("dummy", false)
    private var displaySlot = ObjectiveDisplaySlot.SIDEBAR
    private var sortOrder = ObjectiveSortOrder.ASCENDING
    private var objectiveName = UUID.randomUUID().toString()
    fun build(): SimpleScoreboard {
        return SimpleScoreboard(displaySlot, sortOrder, criteria, objectiveName)
    }
}