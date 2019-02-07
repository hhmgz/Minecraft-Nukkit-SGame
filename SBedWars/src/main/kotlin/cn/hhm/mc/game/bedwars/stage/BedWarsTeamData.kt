package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.utils.asString
import cn.nukkit.level.Location

data class BedWarsTeamData(var id: Int, var spawnLocation: Location, var shopLocation: Location, var coreLocation: Location, var maxOfPlayers: Int) {
    val players: ArrayList<String> = arrayListOf()

    fun toMap() = hashMapOf("spawnLocation" to spawnLocation.asString(), "shopLocation" to shopLocation.asString(), "coreLocation" to coreLocation.asString(), "maxOfPlayers" to maxOfPlayers)
}