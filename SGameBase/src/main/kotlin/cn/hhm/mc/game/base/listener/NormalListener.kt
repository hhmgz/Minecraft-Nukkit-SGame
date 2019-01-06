package cn.hhm.mc.game.base.listener

import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerCreationEvent


/**
 * @author hhm @ SBedWars Project
 */
class NormalListener : Listener {
    @EventHandler
    fun onCreate(event: PlayerCreationEvent) {
        event.baseClass = NukkitPlayer::class.java
        event.playerClass = NukkitPlayer::class.java
    }
}
