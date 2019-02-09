package cn.hhm.mc.game.base.utils.scoreboard

import cn.hhm.mc.game.base.module.AbstractModule
import cn.hhm.mc.game.base.utils.scoreboard.packet.RemoveObjectivePacket
import cn.hhm.mc.game.base.utils.scoreboard.packet.SetDisplayObjectivePacket
import cn.hhm.mc.game.base.utils.scoreboard.packet.SetScorePacket
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveCriteria
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveDisplaySlot
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.ObjectiveSortOrder
import cn.hhm.mc.game.base.utils.scoreboard.scoreboard.SimpleScoreboard

import cn.nukkit.Server
import java.io.File
import java.util.*

/**
 * @author CreeperFace
 */
class ScoreboardAPI(file: File) : AbstractModule("ScoreboardAPI", file) {
    override fun onLoad() {
        Server.getInstance().network.registerPacket(RemoveObjectivePacket.NETWORK_ID, RemoveObjectivePacket::class.java)
        Server.getInstance().network.registerPacket(SetDisplayObjectivePacket.NETWORK_ID, SetDisplayObjectivePacket::class.java)
        Server.getInstance().network.registerPacket(SetScorePacket.NETWORK_ID, SetScorePacket::class.java)
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()

        class Builder internal constructor() {
            private var criteria = ObjectiveCriteria("dummy", false)
            private var displaySlot = ObjectiveDisplaySlot.SIDEBAR
            private var sortOrder = ObjectiveSortOrder.ASCENDING
            private var objectiveName = UUID.randomUUID().toString()

            fun setCriteria(criteria: ObjectiveCriteria): Builder {
                this.criteria = criteria
                return this
            }

            fun setDisplaySlot(displaySlot: ObjectiveDisplaySlot): Builder {
                this.displaySlot = displaySlot
                return this
            }

            fun setSortOrder(sortOrder: ObjectiveSortOrder): Builder {
                this.sortOrder = sortOrder
                return this
            }

            fun setObjectiveName(objectiveName: String): Builder {
                this.objectiveName = objectiveName
                return this
            }

            fun build(): SimpleScoreboard {
                return SimpleScoreboard(displaySlot, sortOrder, criteria, objectiveName)
            }
        }
    }
}