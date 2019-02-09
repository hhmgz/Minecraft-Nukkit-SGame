package cn.hhm.mc.game.base.utils.scoreboard.packet

import cn.nukkit.network.protocol.DataPacket

/**
 * @author CreeperFace
 */
data class RemoveObjectivePacket(var objectiveName: String) : DataPacket() {

    override fun pid() = NETWORK_ID

    override fun encode() {
        reset()
        putString(objectiveName)
    }

    override fun decode() {
        objectiveName = string
    }

    companion object {
        const val NETWORK_ID = 0x6a.toByte()
    }
}