package cn.hhm.mc.game.bedwars.stage.mineral

import cn.hhm.mc.game.bedwars.stage.BedWarsRoom
import cn.nukkit.level.Position

class BedWarsMineralData(room: BedWarsRoom) {
    var diamondDropSpeed = arrayListOf<Int>()
    var goldDropSpeed = arrayListOf<Int>()
    var silverDropSpeed = arrayListOf<Int>()
    var copperDropSpeed = arrayListOf<Int>()
    var diamondStartTime = 60
    var goldStartTime = 20
    var silverStartTime = 5
    var copperStartTime = 1
    val diamondPosition = HashSet<Position>()
    val goldPosition = HashSet<Position>()
    val silverPosition = HashSet<Position>()
    val copperPosition = HashSet<Position>()
}