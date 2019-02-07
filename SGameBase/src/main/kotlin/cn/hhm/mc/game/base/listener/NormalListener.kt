package cn.hhm.mc.game.base.listener

import cn.hhm.mc.game.base.data.PlayerData
import cn.hhm.mc.game.base.utils.GameUtils
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerChatEvent
import cn.nukkit.event.player.PlayerCreationEvent
import cn.nukkit.event.player.PlayerJoinEvent


/**
 * @author hhm @ SBedWars Project
 */
class NormalListener : Listener {
    @EventHandler
    fun onCreate(event: PlayerCreationEvent) {
        event.baseClass = NukkitPlayer::class.java
        event.playerClass = NukkitPlayer::class.java
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player as? NukkitPlayer ?: return
        player.gameData = PlayerData(player.name)
    }

    @EventHandler
    fun onSaying(event: PlayerChatEvent) {
        val player = event.player as? NukkitPlayer ?: return
        if (GameUtils.isPlaying(player)) return
        event.isCancelled = true
        val data = player.gameData
        if (data.isVip) Server.getInstance().broadcastMessage("§6>> §b[§cLv.§2${data.level}§b]§e[${data.titles[data.usedTitle]}§e]§r§o§l§c[§6VIP§c]§r§l§e${data.name}§d: ${event.message}")
        else Server.getInstance().broadcastMessage("§6>> §b[§cLv.§2${data.level}§b]§e[${data.titles[data.usedTitle]}§e]§l§e${data.name}§d: ${event.message}")
    }
}