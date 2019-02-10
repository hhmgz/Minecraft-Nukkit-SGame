package cn.hhm.mc.game.bedwars.shop

import cn.hhm.mc.game.base.gui.window.lambda.LambdaCustomGUI
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.inLevel
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.inType
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.typeThree
import cn.nukkit.Player
import cn.nukkit.item.Item
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.math.roundToInt

class GUIShopThirdLevel(val secondLevel: GUIShopSecondLevel, val id: Int, val iid: String, val copper: Int, val silver: Int, val gold: Int, val diamond: Int, dn: String) {
    var dn: String? = null
        private set
    val base = LambdaCustomGUI("bedwars_shop_third_${secondLevel.id}#${this.id}", "$TITLE §e游戏商店")

    init {
        this.dn = dn.replace("{c}", copper.toString()).replace("{s}", silver.toString()).replace("{g}", gold.toString()).replace("{d}", diamond.toString())
        base.addText("bedwars_shop_third_${secondLevel.id}#${this.id}_text", this.dn!!)
        base.addSlider("bedwars_shop_third_${secondLevel.id}#${this.id}_slider", "数量", 1f, 64f, 1, 1f)
        base.closedClickedListener = Consumer {
            val name = it.name
            typeThree.remove(name)
            inLevel.remove(name)
            inType.remove(name)
        }
        base.submittedClickedListener = BiConsumer { t, u ->
            val name = u.name
            typeThree.remove(name)
            inLevel.remove(name)
            inType.remove(name)
            val count = t["bedwars_shop_third_${secondLevel.id}#${this.id}_slider"].toString().toFloat().roundToInt()
            val nc = copper * count
            val ns = silver * count
            val ng = gold * count
            val nd = diamond * count
            if (hasEnoughResources(u, nc, ns, ng, nd)) {
                this.removeItem(u, Item.get(266, 0, ng))
                this.removeItem(u, Item.get(265, 0, ns))
                this.removeItem(u, Item.get(336, 0, nc))
                this.removeItem(u, Item.get(264, 0, nd))
                for (i in 0 until count) {
                    u.inventory.addItem(GUIShopBase.itemInfo[iid]!!["item"] as Item)
                }
                u.sendMessage("$TITLE§3成功兑换!")
            } else {
                u.sendMessage("$TITLE§c你的资源不足!")
            }
        }
    }

    fun openGUI(player: Player) {
        if (player !is NukkitPlayer) return
        player.showGUI(base)
    }

    fun removeItem(player: Player, vararg slots: Item) {
        val itemSlots = ArrayList<Item>()
        for (slot in slots) {
            if (slot.id != 0 && slot.getCount() > 0) {
                itemSlots.add(slot.clone())
            }
        }
        for (i in 0 until player.inventory.size) {
            val item = player.inventory.getItem(i)
            if (item.id == Item.AIR || item.getCount() <= 0) {
                continue
            }
            for (slot in ArrayList(itemSlots)) {
                if (slot.equals(item, false, false)) {
                    val amount = Math.min(item.getCount(), slot.getCount())
                    slot.setCount(slot.getCount() - amount)
                    item.setCount(item.getCount() - amount)
                    player.inventory.setItem(i, item)
                    if (slot.getCount() <= 0) {
                        itemSlots.remove(slot)
                    }
                }
            }
            if (itemSlots.size == 0) {
                break
            }
        }
    }

    companion object {
        fun getResourceCount(player: Player, type: Int): Int {
            var a = 0
            when (type) {
                1 -> {
                    for (item in player.inventory.slots.values) {
                        if (item.customName == SBedWars.copper.customName) {
                            a += item.getCount()
                        }
                    }
                    return a
                }
                2 -> {
                    for (item in player.inventory.slots.values) {
                        if (item.customName == SBedWars.silver.customName) {
                            a += item.getCount()
                        }
                    }
                    return a
                }
                3 -> {
                    for (item in player.inventory.slots.values) {
                        if (item.customName == SBedWars.gold.customName) {
                            a += item.getCount()
                        }
                    }
                    return a
                }
                4 -> {
                    for (item in player.inventory.slots.values) {
                        if (item.customName == SBedWars.diamond.customName) {
                            a += item.getCount()
                        }
                    }
                    return a
                }
            }
            return -1
        }

        private fun hasEnoughResources(player: Player, copper: Int, silver: Int, gold: Int, diamond: Int): Boolean {
            return getResourceCount(player, 1) >= copper && getResourceCount(player, 2) >= silver && getResourceCount(player, 3) >= gold && getResourceCount(player, 4) >= diamond
        }
    }
}
