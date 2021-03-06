package cn.hhm.mc.game.base.utils

/**
 * @author hhm @ SBedWars Project
 */
object ItemName {
    fun getItemName(id: Int, damage: Int): String {
        val name = if (damage != 0) "$id:$damage" else id.toString()
        when (name) {
            "1" -> return "石头"
            "2" -> return "草块"
            "3" -> return "泥土"
            "4" -> return "圆石"
            "5" -> return "橡木木板"
            "5:1" -> return "云杉木板"
            "5:2" -> return "桦木木板"
            "5:3" -> return "丛林木板"
            "6" -> return "橡树树苗"
            "6:1" -> return "云杉树苗"
            "6:2" -> return "桦木树苗"
            "6:3" -> return "丛林树苗"
            "12" -> return "沙子"
            "13" -> return "沙硕"
            "14" -> return "金矿石"
            "15" -> return "铁矿石"
            "16" -> return "煤矿石"
            "17" -> return "橡木"
            "17:1" -> return "云杉木"
            "17:2" -> return "白桦木 "
            "17:3" -> return "丛林木"
            "18" -> return "橡木树叶"
            "18:1" -> return "云杉树叶"
            "18:2" -> return "白桦树叶"
            "18:3" -> return "丛林树叶"
            "20" -> return "玻璃"
            "21" -> return "青金石矿石"
            "22" -> return "青金石块"
            "23" -> return "发射器"
            "24" -> return "沙石"
            "24:1" -> return "錾制沙石"
            "24:2" -> return "平滑沙石"
            "25" -> return "音符盒"
            "27" -> return "动力铁轨"
            "28" -> return "探测铁轨"
            "29" -> return "粘性活塞"
            "31" -> return "灌木"
            "32" -> return "枯死的灌木"
            "33" -> return "活塞"
            "35" -> return "白色羊毛"
            "35:1" -> return "橙色羊毛"
            "35:2" -> return "红色羊毛"
            "35:3" -> return "蓝色羊毛"
            "35:4" -> return "黄色羊毛"
            "35:5" -> return "灰色羊毛"
            "35:6" -> return "粉色羊毛"
            "35:7" -> return "灰色羊毛"
            "35:8" -> return "淡灰色羊毛"
            "35:9" -> return "青色羊毛"
            "35:10" -> return "紫色羊毛"
            "35:11" -> return "蓝色羊毛"
            "35:12" -> return "棕色羊毛"
            "35:13" -> return "深绿色羊毛"
            "35:14" -> return "红色羊毛"
            "35:15" -> return "黑色羊毛"
            "37" -> return "蒲公英"
            "38" -> return "玫瑰"
            "39" -> return "棕色蘑菇"
            "40" -> return "红色蘑菇"
            "41" -> return "金块"
            "42" -> return "铁块"
            "44" -> return "石台阶"
            "44:1" -> return "沙石台阶"
            "44:2" -> return "木台阶"
            "44:3" -> return "圆石台阶"
            "44:4" -> return "砖台阶"
            "44:5" -> return "石砖台阶"
            "45" -> return "红砖"
            "46" -> return "TNT"
            "47" -> return "书架"
            "48" -> return "苔石"
            "49" -> return "黑曜石"
            "50" -> return "火把"
            "53" -> return "橡木楼梯"
            "54" -> return "箱子"
            "56" -> return "钻石矿石"
            "57" -> return "钻石块"
            "58" -> return "工作台"
            "61" -> return "熔炉"
            "65" -> return "梯子"
            "66" -> return "铁轨"
            "67" -> return "石楼梯"
            "69" -> return "拉杆"
            "70" -> return "石压力板"
            "72" -> return "木压力板"
            "73" -> return "红石矿石"
            "76" -> return "红石火把"
            "77" -> return "石按钮"
            "79" -> return "冰"
            "80" -> return "雪"
            "81" -> return "仙人掌"
            "82" -> return "粘土"
            "84" -> return "唱片机"
            "85" -> return "栅栏"
            "86" -> return "南瓜"
            "87" -> return "地狱岩"
            "88" -> return "灵魂沙"
            "89" -> return "萤石"
            "91" -> return "南瓜灯"
            "96" -> return "活板门"
            "98" -> return "石砖"
            "98:1" -> return "苔石砖"
            "98:2" -> return "裂石砖"
            "98:3" -> return "錾制石砖"
            "99" -> return "蘑菇"
            "100" -> return "蘑菇"
            "101" -> return "铁栅栏"
            "102" -> return "玻璃板"
            "103" -> return "西瓜"
            "106" -> return "藤蔓"
            "107" -> return "栅栏门"
            "108" -> return "砖楼梯"
            "109" -> return "石楼梯"
            "112" -> return "地狱砖块"
            "113" -> return "地狱砖栅栏"
            "114" -> return "地狱砖楼梯"
            "116" -> return "附魔台"
            "121" -> return "末地石"
            "122" -> return "龙蛋"
            "123" -> return "红石灯"
            "125" -> return "桦木楼梯"
            "126" -> return "橡木台阶"
            "126:1" -> return "云杉台阶"
            "126:2" -> return "桦木台阶"
            "126:3" -> return "丛林台阶"
            "128" -> return "沙石楼梯"
            "129" -> return "绿宝石矿石"
            "130" -> return "末影箱"
            "131" -> return ""
            "133" -> return "绿宝石矿石"
            "134" -> return "云杉木楼梯"
            "135" -> return "桦木楼梯"
            "136" -> return "丛林木楼梯"
            "138" -> return "信标"
            "139" -> return "圆石墙"
            "139:1" -> return "苔石墙"
            "143" -> return "按钮"
            "145" -> return "铁砧"
            "145:1" -> return "轻度损耗的铁砧"
            "145:2" -> return "重度损耗的铁砧"
            "146" -> return "陷阱箱"
            "147" -> return "轻质测重压力板"
            "148" -> return "重质测重压力板"
            "151" -> return "阳光传感器"
            "152" -> return "红石块"
            "153" -> return "下界石英矿石"
            "155" -> return "石英方块"
            "156" -> return "石英楼梯"
            "157" -> return "双木台阶"
            "158" -> return "木台阶"
            "170" -> return "干草块"
            "171" -> return "地毯"
            "173" -> return "煤炭块"
            "256" -> return "铁铲子"
            "257" -> return "铁镐"
            "258" -> return "铁斧"
            "259" -> return "打火石"
            "261" -> return "弓"
            "262" -> return "箭"
            "263" -> return "煤炭"
            "263:1" -> return "木炭"
            "264" -> return "钻石"
            "265" -> return "铁锭"
            "266" -> return "金锭"
            "267" -> return "铁剑"
            "268" -> return "木剑"
            "269" -> return "木铲"
            "270" -> return "木镐"
            "271" -> return "木斧"
            "272" -> return "石剑"
            "273" -> return "石铲"
            "274" -> return "石镐"
            "275" -> return "石斧"
            "276" -> return "钻石剑"
            "277" -> return "钻石铲"
            "278" -> return "钻石镐"
            "279" -> return "钻石斧"
            "280" -> return "木棍"
            "281" -> return "碗"
            "282" -> return "蘑菇汤"
            "283" -> return "金剑"
            "284" -> return "金锹"
            "285" -> return "金镐"
            "286" -> return "金斧"
            "287" -> return "线"
            "288" -> return "羽毛"
            "289" -> return "火药"
            "290" -> return "木锄"
            "291" -> return "石锄"
            "292" -> return "铁锄"
            "293" -> return "钻石锄"
            "294" -> return "金锄"
            "295" -> return "小麦种子"
            "296" -> return "小麦"
            "297" -> return "面包"
            "298" -> return "皮革帽子"
            "325" -> return "桶"
            "326" -> return "水桶"
            "327" -> return "岩浆桶"
            "345" -> return "指南针"
            "346" -> return "钓鱼竿"
            "347" -> return "钟"
            "359" -> return "剪刀"
            "260" -> return "苹果"
            "319" -> return "生猪排"
            "320" -> return "熟猪排"
            "322" -> return "金苹果"
            "335" -> return "牛奶"
            "349" -> return "生鱼"
            "350" -> return "熟鱼"
            "353" -> return "糖"
            "354" -> return "蛋糕"
            "357" -> return "曲奇"
            "360" -> return "西瓜"
            "361" -> return "南瓜种子"
            "362" -> return "西瓜种子"
            "363" -> return "生牛肉"
            "364" -> return "牛排"
            "365" -> return "生鸡肉"
            "366" -> return "熟鸡肉"
            "299" -> return "皮革外衣"
            "300" -> return "皮革裤子"
            "301" -> return "皮靴"
            "306" -> return "铁头盔"
            "307" -> return "铁盔甲"
            "308" -> return "铁护腿"
            "309" -> return "铁靴"
            "310" -> return "钻石头盔"
            "311" -> return "钻石胸甲"
            "312" -> return "钻石护腿"
            "313" -> return "钻石靴"
            "314" -> return "金头盔"
            "315" -> return "金护甲"
            "316" -> return "金护腿"
            "317" -> return "金靴子"
            "315:4" -> return "青金石"
            "348" -> return "萤石粉"
            "373" -> return "水瓶"
            "370" -> return "恶魂之泪"
            "371" -> return "金粒"
            "372" -> return "地狱庞"
            "374" -> return "玻璃瓶"
            "375" -> return "蜘蛛眼"
            "376" -> return "发酵的蜘蛛眼"
            "377" -> return "烈焰粉"
            "378" -> return "岩浆膏"
            "381" -> return "末影之眼"
            "382" -> return "闪烁的西瓜"
            "321" -> return "画"
            "323" -> return "告示牌"
            "324" -> return "木门"
            "330" -> return "铁门"
            "336" -> return "红砖"
            "337" -> return "粘土"
            "355" -> return "床"
            "379" -> return "酿造台"
            "380" -> return "炼药锅"
            "388" -> return "绿宝石"
            "351" -> return "墨囊"
            "351:1" -> return "红色玫瑰"
            "351:2" -> return "仙人掌绿"
            "351:3" -> return "可可豆"
            "351:4" -> return "青金石"
            "351:5" -> return "紫色的染料"
            "351:6" -> return "青色的染料"
            "351:7" -> return "浅灰色的染料"
            "351:8" -> return "灰色的染料"
            "351:9" -> return "粉红色染料"
            "351:10" -> return "黄绿色染料"
            "351:11" -> return "蒲公英黄"
            "351:12" -> return "浅蓝色的染料"
            "351:13" -> return "品红染料"
            "351:14" -> return "橙色染料"
            "351:15" -> return "骨粉"
            "318" -> return "燧石"
            "332" -> return "雪球"
            "334" -> return "皮革"
            "338" -> return "甘蔗"
            "339" -> return "纸"
            "340" -> return "书"
            "341" -> return "粘液球"
            "344" -> return "鸡蛋"
            "352" -> return "骨头"
            "367" -> return "腐肉"
            "368" -> return "末影珍珠"
            "369" -> return "火焰棒"
            else -> return "未知"
        }
    }
}
