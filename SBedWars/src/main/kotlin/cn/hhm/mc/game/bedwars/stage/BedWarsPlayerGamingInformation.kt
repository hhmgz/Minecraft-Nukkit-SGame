package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.player.PlayerGamingInformation
import cn.hhm.mc.game.bedwars.SBedWars
import cn.hhm.mc.game.bedwars.stage.team.BedWarsTeamInstance

class BedWarsPlayerGamingInformation(val room: BedWarsRoom, val instance: BedWarsInstance) : PlayerGamingInformation(SBedWars.instance.type, room, instance) {
    var kill = 0
    var death = 0
    var breakCore = 0
    var team: BedWarsTeamInstance? = null
}