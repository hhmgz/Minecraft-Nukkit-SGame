package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.asString
import cn.hhm.mc.game.base.utils.toLevel
import cn.hhm.mc.game.base.utils.toLocation
import cn.hhm.mc.game.base.utils.toPosition
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.stage.task.BedWarsPlayingTask
import cn.hhm.mc.game.bedwars.stage.task.BedWarsWaitTask
import cn.nukkit.Server
import cn.nukkit.level.Level
import cn.nukkit.level.Position
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.collections.MutableMap
import kotlin.collections.arrayListOf
import kotlin.collections.forEach
import kotlin.collections.get
import kotlin.collections.hashMapOf
import kotlin.collections.set

class BedWarsRoom(id: String) : GameRoom(SBedWars.instance.type, id) {
    var disName: String = "${type.mainTitle}-$id"//房间显示名称
    var gameLevel: Level = Server.getInstance().defaultLevel
    var voidKill = 0
    var diamondDropSpeed = 60
    var goldDropSpeed = 20
    var silverDropSpeed = 5
    var copperDropSpeed = 1
    val diamondPosition = HashSet<Position>()
    val goldPosition = HashSet<Position>()
    val silverPosition = HashSet<Position>()
    val copperPosition = HashSet<Position>()
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
        val dropPosition = mineral["position"] as MutableMap<*, *>
        (dropPosition["diamond"] as ArrayList<*>).forEach { this.diamondPosition.add(it.toString().toPosition()) }
        (dropPosition["gold"] as ArrayList<*>).forEach { this.goldPosition.add(it.toString().toPosition()) }
        (dropPosition["silver"] as ArrayList<*>).forEach { this.silverPosition.add(it.toString().toPosition()) }
        (dropPosition["copper"] as ArrayList<*>).forEach { this.copperPosition.add(it.toString().toPosition()) }
        this.diamondDropSpeed = dropSpeed["diamond"] as Int
        this.goldDropSpeed = dropSpeed["gold"] as Int
        this.silverDropSpeed = dropSpeed["silver"] as Int
        this.copperDropSpeed = dropSpeed["copper"] as Int
        (data["teamData"] as MutableMap<*, *>).forEach { k, v ->
            val tData = v as MutableMap<*, *>
            val id = k.toString().toInt()
            teamData[id] = BedWarsTeamData(id, tData["spawnLocation"].toString().toLocation(), tData["shopLocation"].toString().toLocation(), tData["coreLocation"].toString().toLocation(), tData["maxOfPlayers"] as Int)
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
        this.diamondPosition.forEach { dP.add(it.asString()) }
        this.goldPosition.forEach { gP.add(it.asString()) }
        this.copperPosition.forEach { cP.add(it.asString()) }
        this.silverPosition.forEach { sP.add(it.asString()) }
        map["mineral"] = hashMapOf(
                "speed" to hashMapOf("diamond" to diamondDropSpeed, "gold" to goldDropSpeed, "silver" to silverDropSpeed, "copper" to copperDropSpeed),
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