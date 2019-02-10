package cn.hhm.mc.game.base.player

import cn.hhm.mc.game.base.data.PlayerData
import cn.hhm.mc.game.base.gui.NukkitGraphicalUserInterface
import cn.hhm.mc.game.base.gui.function.Variable
import cn.hhm.mc.game.base.utils.scoreboard.ScoreboardAPI
import cn.nukkit.Player
import cn.nukkit.network.SourceInterface
import cn.nukkit.network.protocol.ModalFormRequestPacket


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */

class NukkitPlayer(`interface`: SourceInterface, clientID: Long?, ip: String, port: Int) : Player(`interface`, clientID, ip, port) {
    val guiParams: HashMap<String, Array<out Any>> = hashMapOf()
    var gameInfo: PlayerGamingInformation? = null
    lateinit var gameData: PlayerData
    var scoreboard = ScoreboardAPI.build()

    fun getFormWindowCount() = this.formWindowCount

    fun showGUI(gui: Variable, vararg param: Any) {
        if (gui is NukkitGraphicalUserInterface) this.guiParams[gui.id] = param
        gui.open(this, param)
    }

    fun showGUI(data: String, gui: NukkitGraphicalUserInterface): Int {
        println(data)
        val packet = ModalFormRequestPacket()
        packet.formId = this.formWindowCount++
        packet.data = data
        this.dataPacket(packet)
        gui.openIds[this.name] = packet.formId
        NukkitGraphicalUserInterface.addInGUI(this.name, gui.id)
        return packet.formId
    }

    fun showGUI(gui: NukkitGraphicalUserInterface): Int = showGUI(gui.data, gui)

    fun setScoreboardConnect(title: String, vararg data: String) {
        scoreboard = ScoreboardAPI.build()
        scoreboard.setDisplayName(title)
        var max = 0
        data.forEachIndexed { index, s ->
            max = kotlin.math.max(s.length, max)
            scoreboard.setScore((index + 2).toLong(), s, index + 2)
        }
        var string = " "
        for (i in 0..max) string += '-'
        scoreboard.setScore(1, "$string ", 1)
    }

    fun showScoreboard() {
        scoreboard.addPlayer(this)
    }

    fun hideScoreboard() {
        scoreboard.despawnFrom(this)
    }

    fun sendScoreboard(title: String, data: Array<String>) {
        this.hideScoreboard()
        this.setScoreboardConnect(title, *data)
        this.showScoreboard()
    }
}