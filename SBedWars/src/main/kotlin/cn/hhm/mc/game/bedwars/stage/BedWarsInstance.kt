package cn.hhm.mc.game.bedwars.stage

import cn.hhm.mc.game.base.stage.instance.CanDeathGameInstance
import cn.hhm.mc.game.bedwars.SBedWars
import cn.nukkit.entity.item.EntityItem

class BedWarsInstance(room: BedWarsRoom) : CanDeathGameInstance(room) {
    val realRoom
        get() = room as BedWarsRoom
    val teamData
        get() = realRoom.teamData

    override fun start() {

    }

    override fun stop() {

    }

    override fun waitTick(tick: Int) {

    }

    override fun gamingTick(tick: Int) {
        this.checkTeam()
        this.dropMineral(tick)
    }

    fun win(team: BedWarsTeamData) {

        this.stop()
    }

    fun draw() {

        this.stop()
    }

    fun dropMineral(tick: Int) {
        if (tick % realRoom.diamondDropSpeed == 0) {
            realRoom.diamondPosition.forEach {
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
        if (tick % realRoom.goldDropSpeed == 0) {
            realRoom.goldPosition.forEach {
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
        if (tick % realRoom.diamondDropSpeed == 0) {
            realRoom.diamondPosition.forEach {
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
        if (tick % realRoom.diamondDropSpeed == 0) {
            realRoom.diamondPosition.forEach {
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

    private fun checkTeam() {
        var i = 0
        var team: BedWarsTeamData? = null
        teamData.forEach { _, data ->
            if (data.alivePlayers.size == 0) {
                i++
            } else {
                team = data
            }
        }
        if (i == teamData.size - 1) {
            if (team != null) {
                this.win(team!!)
                this.gamingTask?.cancel()
            }
        }
    }

    override fun onFinished() {
        this.draw()
    }
}