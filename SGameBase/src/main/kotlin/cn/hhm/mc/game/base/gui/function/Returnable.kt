package cn.hhm.mc.game.base.gui.function

import cn.hhm.mc.game.base.gui.NukkitGraphicalUserInterface
import cn.hhm.mc.game.base.player.NukkitPlayer

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
interface Returnable {
    fun setParent(player: NukkitPlayer, gui: NukkitGraphicalUserInterface)

    fun getParent(player: NukkitPlayer): NukkitGraphicalUserInterface?

    fun goBack(player: NukkitPlayer): Boolean {
        val parent = this.getParent(player)
        parent ?: return false
        player.showGUI(parent)
        return true
    }

    fun goBack(player: NukkitPlayer, vararg param: Any): Boolean {
        val parent = this.getParent(player)
        parent ?: return false
        if (parent !is Variable) return false
        player.showGUI(parent as Variable, *param)
        return true
    }
}