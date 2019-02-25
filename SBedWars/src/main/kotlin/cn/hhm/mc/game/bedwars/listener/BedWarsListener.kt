package cn.hhm.mc.game.bedwars.listener

import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.base.stage.StageMode
import cn.hhm.mc.game.bedwars.stage.BedWarsPlayerGamingInformation
import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.entity.EntityDamageByEntityEvent
import cn.nukkit.event.player.PlayerCommandPreprocessEvent
import cn.nukkit.event.player.PlayerDeathEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.event.server.DataPacketReceiveEvent
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData
import cn.nukkit.network.protocol.InventoryTransactionPacket

/**
 * SGame
 *
 * Package: cn.hhm.mc.game.bedwars.listener
 * @author hhm Copyright (c) 2019/2/25 21:11
 * version 1.0
 */
class BedWarsListener : Listener {
    @EventHandler
    fun onBreakCore(event: DataPacketReceiveEvent) {
        if (event.packet !is InventoryTransactionPacket) return
        val pk = event.packet as InventoryTransactionPacket
        if (pk.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY) return
        val player = event.player as? NukkitPlayer ?: return
        val gi = player.gameInfo as? BedWarsPlayerGamingInformation ?: return
        if (gi.instance.stage != StageMode.GAMING) return
        val data = pk.transactionData as UseItemOnEntityData
        if (data.actionType != 1) return
        event.isCancelled = gi.instance.attackCrystal(player, data.itemInHand, data.entityRuntimeId)
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity as? NukkitPlayer ?: return
        val gi = player.gameInfo as? BedWarsPlayerGamingInformation ?: return
        gi.instance.onDie(event, player)
    }

    @EventHandler
    fun onImmuneDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as? NukkitPlayer ?: return
        val entity = event.entity  as? NukkitPlayer ?: return
        val dgi = damager.gameInfo as? BedWarsPlayerGamingInformation ?: return
        val egi = entity.gameInfo as? BedWarsPlayerGamingInformation ?: return
        if (event.damager.getLevel().folderName == "lobby") {
            event.setCancelled()
            return
        }
        if (dgi.instance != egi.instance) {
            try {
                event.damage = 0f
            } catch (e: NullPointerException) {
                //辣鸡bug你开心就好
            }
            event.isCancelled = true
            return
        }
        if (dgi.team == egi.team || dgi.instance.stage != StageMode.GAMING) {
            try {
                event.damage = 0f
            } catch (e: NullPointerException) {
                //辣鸡bug你开心就好
            }
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        val player = event.player as? NukkitPlayer ?: return
        if (player.gameInfo !is BedWarsPlayerGamingInformation) return
        if (!event.player.isOp) {
            if (event.message.length < 4 && event.message != "/bw") {
                event.isCancelled = true
                return
            }
            if (event.message.substring(0, 4) != "/bw ") {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player as? NukkitPlayer ?: return
        val gi = player.gameInfo as? BedWarsPlayerGamingInformation ?: return
        gi.instance.quit(player)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.setCheckMovement(false)
    }
}