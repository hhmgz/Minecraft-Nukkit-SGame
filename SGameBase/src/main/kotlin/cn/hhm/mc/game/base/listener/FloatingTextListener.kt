package cn.hhm.mc.game.base.listener

import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.hhm.mc.game.base.utils.NukkitUtils
import cn.nukkit.Server
import cn.nukkit.event.EventHandler
import cn.nukkit.event.EventPriority
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerJoinEvent

class FloatingTextListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player as? NukkitPlayer ?: return
        Server.getInstance().scheduler.scheduleDelayedTask({
            NukkitUtils.floatingTextMap.values.filter { it.pos.level == player.level }.forEach { player.dataPacket(it.addEntityPacket) }
        }, 60)
    }
}