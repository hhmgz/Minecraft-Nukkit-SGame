package cn.hhm.mc.game.bedwars.gui.shop

import cn.hhm.mc.game.base.gui.window.lambda.defaults.LambdaVariableButtonGUI
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.nukkit.Player
import cn.nukkit.utils.Config
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

object BedWarsGUIShopBase {
    var typeThree = HashMap<String, Int>()
    var inType = HashMap<String, Int>()
    var inLevel = HashMap<String, Int>()
    var itemInfo = HashMap<String, HashMap<String, Any>>()
    var second = HashMap<Int, BedWarsGUIShopSecondLevel>()
    val base: LambdaVariableButtonGUI = LambdaVariableButtonGUI("bedwars_shop_base", "$TITLE §e游戏商店", "你拥有: §6铜x%0 §f银x%1 §e金x%2 §2钻石x%3")

    fun init() {
        base.buttonClickedListener = BiConsumer { t, u ->
            val name = u.name
            inLevel[name] = 2
            inType[name] = t + 1
            second[t + 1]!!.openGUI(u)
        }
        base.closedClickedListener = Consumer {
            val name = it.name
            typeThree.remove(name)
            inLevel.remove(name)
            inType.remove(name)
        }
        base.addButton("bedwars_shop_main_button#1", "武器")
        base.addButton("bedwars_shop_main_button#2", "防具")
        base.addButton("bedwars_shop_main_button#3", "食物")
        base.addButton("bedwars_shop_main_button#4", "药水")
        base.addButton("bedwars_shop_main_button#5", "方块")
        base.addButton("bedwars_shop_main_button#6", "工具")
        base.addButton("bedwars_shop_main_button#7", "道具")
        base.addButton("bedwars_shop_main_button#8", "其他")
        load()
    }

    fun load() {
        val config = Config(SBedWars.instance.absolutePath + "/shop.yml")
        second[1] = BedWarsGUIShopSecondLevel(1, config.get("Attack", HashMap()))
        second[2] = BedWarsGUIShopSecondLevel(2, config.get("Defense", HashMap()))
        second[3] = BedWarsGUIShopSecondLevel(3, config.get("Food", HashMap()))
        second[4] = BedWarsGUIShopSecondLevel(4, config.get("Effect", HashMap()))
        second[5] = BedWarsGUIShopSecondLevel(5, config.get("Block", HashMap()))
        second[6] = BedWarsGUIShopSecondLevel(6, config.get("Tool", HashMap()))
        second[7] = BedWarsGUIShopSecondLevel(7, config.get("Prop", HashMap()))
        second[8] = BedWarsGUIShopSecondLevel(8, config.get("Other", HashMap()))
    }

    fun openMain(player: Player) {
        if (player !is NukkitPlayer) return
        val name = player.name
        inLevel[name] = 1
        player.showGUI(base, arrayOf(BedWarsGUIShopThirdLevel.getResourceCount(player, 1), BedWarsGUIShopThirdLevel.getResourceCount(player, 2), BedWarsGUIShopThirdLevel.getResourceCount(player, 3), BedWarsGUIShopThirdLevel.getResourceCount(player, 4)))
    }
}
