package cn.hhm.mc.game.bedwars.stage.mineral

import cn.hhm.mc.game.base.utils.NukkitUtils
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.stage.BedWarsInstance
import cn.nukkit.entity.item.EntityItem
import cn.nukkit.item.Item

class BedWarsMineralInstance(private val instance: BedWarsInstance, private val data: BedWarsMineralData) {
    var brushed = false
    var diamondLevel: Int = 1
    var goldLevel: Int = 1
    var goldNeed = 0
    var diamondNeed = 0
    private val levelName = arrayOf("", "I", "II", "III")

    fun tick(tick: Int) {
        this.updateTime(tick)
        this.brushFloatingText()
        this.dropMineral()
    }

    private fun updateTime(tick: Int) {
        diamondNeed = (tick - data.diamondStartTime) % data.diamondDropSpeed[diamondLevel]
        goldNeed = (tick - data.goldStartTime) % data.goldDropSpeed[goldLevel]
        instance.teamInstance.forEach { _, u ->
            u.silverNeed = (tick - data.silverStartTime) % data.silverDropSpeed[u.silverLevel]
            u.copperNeed = (tick - data.copperStartTime) % data.copperDropSpeed[u.copperLevel]
        }
    }

    private fun brushFloatingText() {
        if (brushed) {
            data.diamondPosition.forEach {
                it.level.dropItem(it.add(0.0, 3.0), Item.get(57, 0, 1))
                NukkitUtils.setFloatingText("bw.${instance.name}.mineral.diamond.${it.hashCode()}", "§b钻石 §f产矿机", "§3等级: §b${levelName[diamondLevel]}\n§7|| §aSPAWN IN §e" + diamondNeed + "s")
                TODO("并不是很清楚DropItem的设置")
            }
            data.goldPosition.forEach {
                it.level.dropItem(it.add(0.0, 3.0), Item.get(41, 0, 1))
                NukkitUtils.setFloatingText("bw.${instance.name}.mineral.gold.${it.hashCode()}", "§6黄金 §f产矿机", "§3等级: §b${levelName[goldLevel]}\n§7|| §aSPAWN IN §e" + goldNeed + "s")
            }
            instance.teamInstance.forEach { _, u ->
                u.team.mineralData.silverPosition.forEach {
                    it.level.dropItem(it.add(0.0, 3.0), Item.get(42, 0, 1))
                    NukkitUtils.setFloatingText("bw.${instance.name}.mineral.silver.${it.hashCode()}", "§6${u.team.name} §7 - §f白银 §f产矿机", "§3等级: §b${levelName[u.silverLevel]}\n§7|| §aSPAWN IN §e" + u.silverNeed + "s")
                }
                u.team.mineralData.copperPosition.forEach {
                    it.level.dropItem(it.add(0.0, 3.0), Item.get(159, 12, 1))
                    NukkitUtils.setFloatingText("bw.${instance.name}.mineral.copper.${it.hashCode()}", "§6${u.team.name} §7 - §d铜矿 §f产矿机", "§3等级: §b${levelName[u.copperLevel]}\n§7|| §aSPAWN IN §e" + u.copperNeed + "s")
                }
            }
        } else {
            data.diamondPosition.forEach {
                NukkitUtils.addFloatingText("bw.${instance.name}.mineral.diamond.${it.hashCode()}", "§b钻石 §f产矿机", "§3等级: §b${levelName[diamondLevel]}\n§7|| §aSPAWN IN §e" + diamondNeed + "s", it)
            }
            data.goldPosition.forEach {
                NukkitUtils.addFloatingText("bw.${instance.name}.mineral.gold.${it.hashCode()}", "§6黄金 §f产矿机", "§3等级: §b${levelName[goldLevel]}\n§7|| §aSPAWN IN §e" + goldNeed + "s", it)
            }
            instance.teamInstance.forEach { _, u ->
                u.team.mineralData.silverPosition.forEach {
                    NukkitUtils.addFloatingText("bw.${instance.name}.mineral.silver.${it.hashCode()}", "§6${u.team.name} §7 - §f白银 §f产矿机", "§3等级: §b${levelName[u.silverLevel]}\n§7|| §aSPAWN IN §e" + u.silverNeed + "s", it)
                }
                u.team.mineralData.copperPosition.forEach {
                    NukkitUtils.addFloatingText("bw.${instance.name}.mineral.copper.${it.hashCode()}", "§6${u.team.name} §7 - §d铜矿 §f产矿机", "§3等级: §b${levelName[u.copperLevel]}\n§7|| §aSPAWN IN §e" + u.copperNeed + "s", it)
                }
            }
        }
    }

    private fun dropMineral() {
        if (diamondNeed == 0) {
            data.diamondPosition.forEach {
                var count = 0
                for (entity in it.level.entities) {
                    if (entity is EntityItem) {
                        if (entity.item.id == SBedWars.diamond.id) {
                            if (entity.distance(it) < 3) {
                                count++
                            }
                        }
                    }
                }
                if (count < 6) { //点位最大生成数
                    it.level.dropItem(it, SBedWars.diamond)
                }
            }
        }
        if (goldNeed == 0) {
            data.goldPosition.forEach {
                var count = 0
                for (entity in it.level.entities) {
                    if (entity is EntityItem) {
                        if (entity.item.id == SBedWars.gold.id) {
                            if (entity.distance(it) < 3) {
                                count++
                            }
                        }
                    }
                }
                if (count < 24) { //点位最大生成数
                    it.level.dropItem(it, SBedWars.gold)
                }
            }
        }
        instance.teamInstance.forEach { _, u ->
            if (u.silverNeed == 0) {
                u.team.mineralData.silverPosition.forEach {
                    var count = 0
                    for (entity in it.level.entities) {
                        if (entity is EntityItem) {
                            if (entity.item.id == SBedWars.silver.id) {
                                if (entity.distance(it) < 3) {
                                    count++
                                }
                            }
                        }
                    }
                    if (count < 96) { //点位最大生成数
                        it.level.dropItem(it, SBedWars.silver)
                    }
                }
            }
            if (u.copperNeed == 0) {
                u.team.mineralData.copperPosition.forEach {
                    var count = 0
                    for (entity in it.level.entities) {
                        if (entity is EntityItem) {
                            if (entity.item.id == SBedWars.copper.id) {
                                if (entity.distance(it) < 3) {
                                    count++
                                }
                            }
                        }
                    }
                    if (count < 192) { //点位最大生成数
                        it.level.dropItem(it, SBedWars.copper)
                    }
                }
            }
        }
    }
}