package cn.hhm.mc.game.bedwars.stage.team

class BedWarsTeamInstance(val team: BedWarsTeamData) {
    val players: ArrayList<String> = arrayListOf()
    val alivePlayers: ArrayList<String> = arrayListOf()
    var copperLevel: Int = 1
    var silverLevel: Int = 1
    var copperNeed = 0
    var silverNeed = 0
}