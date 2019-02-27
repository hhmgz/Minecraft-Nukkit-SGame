package cn.hhm.mc.game.bedwars.gui.configure

import cn.hhm.mc.game.base.gui.window.lambda.LambdaCustomGUI
import cn.hhm.mc.game.base.gui.window.lambda.defaults.LambdaVariableButtonGUI
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.hhm.mc.game.bedwars.stage.BedWarsRoom
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * SGame
 *
 * Package: cn.hhm.mc.game.bedwars.gui.configure
 * @author hhm Copyright (c) 2019/2/27 21:44
 * version 1.0
 */
object BedWarsConfigureGUI {
    val playerConfigure: HashMap<String, BedWarsRoom> = hashMapOf()
    val mainGUI = LambdaVariableButtonGUI("bw_roomConfigure_main", "$TITLE 房间实例配置", "§6§l%0,欢迎你!%1.")
    val newConfigurationGUI = LambdaCustomGUI("bw_roomConfigure_new", "$TITLE 新建房间配置实例")

    fun init() {
        mainGUI.addButton("new", "新建房间配置实例", Consumer {
            it.showGUI(newConfigurationGUI)
        })
        newConfigurationGUI.addInput("rid", "房间ID")
        newConfigurationGUI.addInput("dn", "显示名称", TITLE)
        newConfigurationGUI.addSlider("minp", "最小人数", 2f, 32f, 1, 4f)
        newConfigurationGUI.addSlider("maxp", "最大人数", 4f, 128f, 1, 16f)
        newConfigurationGUI.addSlider("teamNumber", "团队数量", 2f, 16f, 1, 4f)
        newConfigurationGUI.closedClickedListener = Consumer {
            it.sendMessage(TITLE + "你已经放弃房间实例的配置!")
            playerConfigure.remove(it.name)
        }
        newConfigurationGUI.submittedClickedListener = BiConsumer { data, it ->
            val id = data["rid"].toString()
            if (SBedWars.instance.gameRooms.containsKey(id)) {
                it.sendMessage(TITLE + "已经有此ID!")
                return@BiConsumer
            }
            val room = BedWarsRoom(id)
            room.minOfPlayers = data["minp"].toString().toFloat().toInt()
            room.maxOfPlayers = data["maxp"].toString().toFloat().toInt()
            room.teamCount = data["teamNumber"].toString().toFloat().toInt()
            playerConfigure[it.name] = room
            it.sendMessage(TITLE + "请继续进行配置!")
        }
    }

    fun openMainGUI(player: NukkitPlayer) {
        player.showGUI(mainGUI, arrayOf(player.name, if (playerConfigure.containsKey(player.name)) "请继续配置房间${playerConfigure[player.name]!!.id}" else "请进行新建房间配置操作"))
    }
}