package cn.hhm.mc.game.bedwars.command

import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.SBedWars.Companion.TITLE
import cn.hhm.mc.game.bedwars.gui.configure.BedWarsConfigureGUI
import cn.hhm.mc.game.bedwars.gui.shop.BedWarsGUIShopBase
import cn.hhm.mc.game.bedwars.stage.BedWarsPlayerGamingInformation
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender

/**
 * SGame
 *
 * Package: cn.hhm.mc.game.bedwars.command
 * @author hhm Copyright (c) 2019/2/27 21:30
 * version 1.0
 */
class BedWarsCommand : Command("bw") {
    override fun execute(sender: CommandSender, s: String, strings: Array<out String>): Boolean {
        if (sender !is NukkitPlayer) {
            sender.sendMessage(TITLE + "你不是一个玩家!")
            return false
        }
        if (strings.isEmpty()) {
            sender.sendMessage(TITLE + "输入错误,请输入/bw help 查看帮助")
            return false
        }
        when (strings[0]) {
            "open" -> {
                BedWarsGUIShopBase.openMain(sender)
            }
            "join" -> {
                if (strings.size != 2) {
                    sender.sendMessage(TITLE + "输入错误,请输入/bw help 查看帮助")
                    return false
                }
                val gi = sender.gameInfo
                if (gi != null) {
                    sender.sendMessage(TITLE + "你已经在游戏中了,无法加入")
                    return true
                }
                if (!SBedWars.instance.gameRooms.containsKey(strings[1])) {
                    sender.sendMessage(TITLE + "没有此房间,请确保输入正确")
                    return true
                }
                SBedWars.instance.gameRooms[strings[1]]!!.join(sender)
                return true
            }
            "quit" -> {
                val gi = sender.gameInfo
                if (gi == null || gi !is BedWarsPlayerGamingInformation) {
                    sender.sendMessage(TITLE + "你并不在起床战争游戏中,无法退出")
                    return true
                }
                gi.instance.quit(sender, true)
            }
            "set" -> {
                if (!sender.isOp) {
                    sender.sendMessage("$TITLE§c你没有权限!")
                    return false
                }
                BedWarsConfigureGUI.openMainGUI(sender)
            }
            "help" -> {
                sender.sendMessage("§6§l====$TITLE§6====")
                sender.sendMessage("§a/bw help - 查询帮助")
                sender.sendMessage("§b/bw join <房间id> - 加入房间")
                sender.sendMessage("§c/bw quit - 退出房间")
                sender.sendMessage("§d/bw set - 设置房间")
            }
        }
        return true
    }
}