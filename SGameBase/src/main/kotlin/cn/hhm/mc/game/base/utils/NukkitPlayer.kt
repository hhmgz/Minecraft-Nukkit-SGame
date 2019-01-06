package cn.hhm.mc.game.base.utils

import cn.hhm.mc.game.base.gui.NukkitGraphicalUserInterface
import cn.hhm.mc.game.base.gui.function.Variable
import cn.nukkit.Player
import cn.nukkit.network.SourceInterface
import cn.nukkit.network.protocol.ModalFormRequestPacket


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */

class NukkitPlayer(interfaz: SourceInterface, clientID: Long?, ip: String, port: Int) : Player(interfaz, clientID, ip, port) {
    val guiParams: HashMap<String, Array<out Any>> = hashMapOf()

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
}