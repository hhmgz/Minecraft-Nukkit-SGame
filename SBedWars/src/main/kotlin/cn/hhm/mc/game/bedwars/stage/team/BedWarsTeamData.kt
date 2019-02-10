package cn.hhm.mc.game.bedwars.stage.team

import cn.hhm.mc.game.base.utils.asString
import cn.nukkit.level.Location

data class BedWarsTeamData(var id: Int, var name: String, var spawnLocation: Location, var shopLocation: Location, var coreLocation: Location, var maxOfPlayers: Int) {
    val mineralData = BedWarsTeamMineralData(this)

    fun toMap() = hashMapOf("name" to name, "spawnLocation" to spawnLocation.asString(), "shopLocation" to shopLocation.asString(), "coreLocation" to coreLocation.asString(), "maxOfPlayers" to maxOfPlayers)

    fun createInstance() = BedWarsTeamInstance(this)
}