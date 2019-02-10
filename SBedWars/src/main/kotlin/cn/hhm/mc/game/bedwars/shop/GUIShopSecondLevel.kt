package cn.hhm.mc.game.bedwars.shop

import cn.hhm.mc.game.base.gui.window.lambda.defaults.LambdaVariableButtonGUI
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.base.utils.ItemName
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.inLevel
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.inType
import cn.hhm.mc.game.bedwars.shop.GUIShopBase.typeThree
import cn.nukkit.Player
import cn.nukkit.item.Item
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.nbt.tag.CompoundTag
import java.util.*
import java.util.function.Consumer

class GUIShopSecondLevel(val id: Int, data: HashMap<String, Any>) {
    val third: HashMap<Int, GUIShopThirdLevel> = HashMap()
    val base: LambdaVariableButtonGUI = LambdaVariableButtonGUI("bedwars_shop_second_base#$id", "$TITLE §e游戏商店", "你拥有: §6铜x%0 §f银x%1 §e金x%2 §2钻石x%3")

    init {
        for (key in data.keys) {
            this.setItem(key, data[key] as HashMap<String, Any>)
        }
        this.setGUI()
    }

    fun setItem(key: String, data: HashMap<String, Any>) {
        val info = HashMap<String, Any>()
        val need = data["Need"] as Map<String, Any>
        info["copper"] = Integer.valueOf(need["Copper"].toString())
        info["silver"] = Integer.valueOf(need["Silver"].toString())
        info["gold"] = Integer.valueOf(need["Gold"].toString())
        info["diamond"] = Integer.valueOf(need["Diamond"].toString())
        info["dn"] = data["DisplayName"]!!
        info["in"] = data["Name"]!!
        info["ii"] = data["Item"]!!
        info["co"] = data["Count"]!!
        info["en"] = data["Enchant"]!!
        info["ne"] = need
        val ids = data["Item"].toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val item = Item.get(Integer.valueOf(ids[0]), Integer.valueOf(ids[1]), Integer.valueOf(data["Count"].toString()))
        if (data["Name"] != "default") {
            item.customName = TITLE + data["Name"].toString()
        } else {
            item.customName = TITLE + ItemName.getItemName(item.id, item.damage)
        }
        val enchant = data["Enchant"] as ArrayList<*>
        if (!enchant.isEmpty()) {
            for (mlgb in enchant) {
                val es = mlgb.toString().split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val enchantment = Enchantment.get(Integer.valueOf(es[0]))
                enchantment.level = Integer.valueOf(es[1])
                item.addEnchantment(enchantment)
            }
        }
        item.customBlockData = CompoundTag().putString("bid", key)
        info["item"] = item
        GUIShopBase.itemInfo[key] = info
        third[third.size] = GUIShopThirdLevel(this, third.size,
                key,
                info["copper"] as Int,
                info["silver"] as Int,
                info["gold"] as Int,
                info["diamond"] as Int,
                info["dn"].toString())
    }

    fun setGUI() {
        for (gt in third.values) {
            base.addButton("bedwars_shop_second_button_$id#${gt.id}", gt.dn!!, Consumer {
                val name = it.name
                inLevel[name] = 3
                typeThree[name] = gt.id
                third[gt.id]!!.openGUI(it)
            })
        }
        base.addButton("bedwars_shop_second_button_$id#goBack", "§3返回上一级", Consumer {
            base.goBack(it as NukkitPlayer, arrayOf(GUIShopThirdLevel.getResourceCount(it, 1), GUIShopThirdLevel.getResourceCount(it, 2), GUIShopThirdLevel.getResourceCount(it, 3), GUIShopThirdLevel.getResourceCount(it, 4)))
        })
        base.closedClickedListener = Consumer {
            val name = it.name
            typeThree.remove(name)
            inLevel.remove(name)
            inType.remove(name)
        }
    }

    fun openGUI(player: Player) {
        if (player !is NukkitPlayer) return
        player.showGUI(this.base, arrayOf(GUIShopThirdLevel.getResourceCount(player, 1), GUIShopThirdLevel.getResourceCount(player, 2), GUIShopThirdLevel.getResourceCount(player, 3), GUIShopThirdLevel.getResourceCount(player, 4)))
    }
}
