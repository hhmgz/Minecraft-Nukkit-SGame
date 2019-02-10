package cn.hhm.mc.game.base.utils

import cn.hhm.mc.game.base.utils.scoreboard.packet.RemoveObjectivePacket
import cn.hhm.mc.game.base.utils.scoreboard.packet.SetDisplayObjectivePacket
import cn.hhm.mc.game.base.utils.scoreboard.packet.SetScorePacket
import cn.nukkit.Server

object NukkitBugFixer {
    fun fix() {
        Server.getInstance().network.registerPacket(RemoveObjectivePacket.NETWORK_ID, RemoveObjectivePacket::class.java)
        Server.getInstance().network.registerPacket(SetDisplayObjectivePacket.NETWORK_ID, SetDisplayObjectivePacket::class.java)
        Server.getInstance().network.registerPacket(SetScorePacket.NETWORK_ID, SetScorePacket::class.java)
    }
}