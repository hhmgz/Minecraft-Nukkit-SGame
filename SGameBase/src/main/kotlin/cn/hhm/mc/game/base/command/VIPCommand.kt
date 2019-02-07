package cn.hhm.mc.game.base.command

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.SGameBase.Companion.TITLE
import cn.hhm.mc.game.base.config.VipConfig
import cn.hhm.mc.game.base.utils.MineCraftRMB
import cn.hhm.mc.game.base.utils.format
import cn.hhm.mc.game.base.utils.toDate
import cn.nukkit.command.Command
import cn.nukkit.command.CommandSender
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import java.util.function.Consumer

class VIPCommand : Command("vip") {
    override fun execute(sender: CommandSender, s: String, strings: Array<out String>): Boolean {
        if (strings.isEmpty()) {
            sender.sendMessage(SGameBase.TITLE + "输入错误,请输入/vip help 查看帮助")
            return true
        }
        when (strings[0]) {
            "buy" -> {
                if (strings.size != 3) {
                    sender.sendMessage(TITLE + "输入错误,请输入/vip help 查看帮助")
                    return false
                }
                val i = try {
                    strings[2].toInt()
                } catch (e: Exception) {
                    sender.sendMessage(TITLE + "输入错误,请输入/vip help 查看帮助")
                    return false
                }
                if (i < 1) {
                    sender.sendMessage(TITLE + "输入错误,月份不可少于1")
                    return true
                }
                MineCraftRMB.pay(sender.name, SGameBase.instance.masterConfig.vipSpend * i, "Buy SGame VIP ${i}个月", Consumer {
                    val json = JSONObject.fromObject(it)
                    val code = json["code"].toString()
                    val data = JSONObject.fromObject(JSONArray.fromObject(json["data"].toString())[0])
                    when (code) {
                        "101" -> {
                            sender.sendMessage(TITLE + "成功购买$i 个月的起床战争VIP!VIP到期时间:${VipConfig.addVip(sender.name, i * 30).toDate().format()},剩余余额:${data["money"]}")
                        }
                        "102" -> {
                            sender.sendMessage(TITLE + "购买失败!你的余额不足!剩余余额:${data["money"]},还需要:${data["need"]}")
                        }
                        else -> {
                            sender.sendMessage(TITLE + "请充值开户!")
                        }
                    }
                })
            }
            "give" -> {

            }
            "my" -> {
                MineCraftRMB.getMoney(sender.name, Consumer {
                    val json = JSONObject.fromObject(it)
                    if (json["code"].toString() == "101") {
                        val data = JSONObject.fromObject(JSONArray.fromObject(json["data"].toString())[0])
                        sender.sendMessage("§l§5===${SGameBase.TITLE} §bMCRMB §cINFO§5===\n" +
                                "§l§a余额: ${data["money"]}\n" +
                                "§l§b历史充值总额: ${data["allcharge"]}\n" +
                                "§l§c历史消费总额:${data["allpay"]}")
                    } else {
                        sender.sendMessage(SGameBase.TITLE + "请充值开户!")
                    }
                })
            }
        }
        return true
    }
}