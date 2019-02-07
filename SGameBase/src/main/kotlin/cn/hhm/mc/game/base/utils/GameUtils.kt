package cn.hhm.mc.game.base.utils

import cn.nukkit.Player
import cn.nukkit.Server

object GameUtils {
    @JvmStatic
    fun isPlaying(id: String) = GameUtils.isPlaying(Server.getInstance().getPlayerExact(id))

    @JvmStatic
    fun isPlaying(player: Player?) = player is NukkitPlayer && !player.gameInfo.isNullOrEmpty()
}