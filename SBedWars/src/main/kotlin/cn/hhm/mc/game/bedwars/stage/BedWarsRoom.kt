package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.*
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.stage.mineral.BedWarsMineralData
import cn.hhm.mc.game.bedwars.stage.task.BedWarsPlayingTask
import cn.hhm.mc.game.bedwars.stage.task.BedWarsWaitTask
import cn.hhm.mc.game.bedwars.stage.team.BedWarsTeamData
import cn.hhm.mc.game.bedwars.stage.team.BedWarsTeamMineralData
import cn.nukkit.Server
import cn.nukkit.level.Level
import java.util.*
import kotlin.collections.set

class BedWarsRoom(id: String) : GameRoom(SBedWars.instance.type, id) {
    var disName: String = "${type.mainTitle}-$id"//房间显示名称
    var gameLevel: Level = Server.getInstance().defaultLevel
    var voidKill = 0
    val mineral: BedWarsMineralData = BedWarsMineralData(this)
    val teamData: HashMap<Int, BedWarsTeamData> = hashMapOf()

    override fun configure(data: MutableMap<String, Any>) {
        if (data.isEmpty()) return
        this.disName = data["disName"].toString()
        this.minOfPlayers = data["minOfPlayers"] as Int
        this.maxOfPlayers = data["maxOfPlayers"] as Int
        this.waitTime = data["waitTime"] as Int
        this.waitLocation = data["waitLocation"].toString().toLocation()
        this.gamingTime = data["gamingTime"] as Int
        this.gameLevel = data["gameLevel"].toString().toLevel()
        this.stopLocation = data["stopLocation"].toString().toLocation()
        this.voidKill = data["voidKill"] as Int
        val mineral = data["mineral"] as MutableMap<*, *>
        val dropSpeed = mineral["speed"] as MutableMap<*, *>
        val startTime = mineral["startTime"] as MutableMap<*, *>
        val dropPosition = mineral["position"] as MutableMap<*, *>
        (dropPosition["diamond"] as ArrayList<*>).forEach { this.mineral.diamondPosition.add(it.toString().toPosition()) }
        (dropPosition["gold"] as ArrayList<*>).forEach { this.mineral.goldPosition.add(it.toString().toPosition()) }
        this.mineral.diamondDropSpeed = dropSpeed["diamond"].cast()
        this.mineral.goldDropSpeed = dropSpeed["gold"].cast()
        this.mineral.silverDropSpeed = dropSpeed["silver"].cast()
        this.mineral.copperDropSpeed = dropSpeed["copper"].cast()
        this.mineral.diamondStartTime = startTime["diamond"].cast()
        this.mineral.goldStartTime = startTime["gold"].cast()
        this.mineral.silverStartTime = startTime["silver"].cast()
        this.mineral.copperStartTime = startTime["copper"].cast()
        (data["teamData"] as MutableMap<*, *>).forEach { k, v ->
            val tData = v as MutableMap<*, *>
            val id = k.toString().toInt()
            val team = BedWarsTeamData(id, tData["name"].toString(), tData["spawnLocation"].toString().toLocation(), tData["shopLocation"].toString().toLocation(), tData["coreLocation"].toString().toLocation(), tData["maxOfPlayers"] as Int)
            val teamMineralData = BedWarsTeamMineralData(team)
            (tData["silver"] as ArrayList<*>).forEach {
                val pos = it.toString().toPosition()
                teamMineralData.silverPosition.add(pos)
                this.mineral.silverPosition.add(pos)
            }
            (tData["copper"] as ArrayList<*>).forEach {
                val pos = it.toString().toPosition()
                teamMineralData.copperPosition.add(pos)
                this.mineral.copperPosition.add(pos)
            }

            teamData[id] = team
        }
    }

    override fun getData(): MutableMap<String, Any> {
        val map = hashMapOf<String, Any>()
        map["disName"] = this.disName
        map["minOfPlayers"] = this.minOfPlayers
        map["maxOfPlayers"] = this.maxOfPlayers
        map["waitTime"] = this.waitTime
        map["waitLocation"] = this.waitLocation.asString()
        map["gamingTime"] = this.gamingTime
        map["gameLevel"] = this.gameLevel.name
        map["stopLocation"] = this.stopLocation.asString()
        map["voidKill"] = this.voidKill
        val dP = arrayListOf<String>()
        val sP = arrayListOf<String>()
        val gP = arrayListOf<String>()
        val cP = arrayListOf<String>()
        this.mineral.diamondPosition.forEach { dP.add(it.asString()) }
        this.mineral.goldPosition.forEach { gP.add(it.asString()) }
        this.mineral.copperPosition.forEach { cP.add(it.asString()) }
        this.mineral.silverPosition.forEach { sP.add(it.asString()) }
        map["mineral"] = hashMapOf(
                "speed" to hashMapOf(
                        "diamond" to this.mineral.diamondDropSpeed,
                        "gold" to this.mineral.goldDropSpeed,
                        "silver" to this.mineral.silverDropSpeed,
                        "copper" to this.mineral.copperDropSpeed),
                "startTime" to hashMapOf(
                        "diamond" to this.mineral.diamondStartTime,
                        "gold" to this.mineral.goldStartTime,
                        "silver" to this.mineral.silverStartTime,
                        "copper" to this.mineral.copperStartTime),
                "position" to hashMapOf(
                        "diamond" to dP,
                        "gold" to gP,
                        "silver" to sP,
                        "copper" to cP
                )
        )
        val td = hashMapOf<String, Any>()
        teamData.forEach { t, u ->
            td[t.toString()] = u.toMap()
        }
        map["teamData"] = td
        return map
    }

    override fun save() {
        this.config ?: return
        this.config!!.clear()
        getData().forEach { t, u -> this.config!![t] = u }
        this.config!!.save()
    }

    init {
        this.waitTaskClass = BedWarsWaitTask::class.java
        this.gamingTaskClass = BedWarsPlayingTask::class.java
    }
}