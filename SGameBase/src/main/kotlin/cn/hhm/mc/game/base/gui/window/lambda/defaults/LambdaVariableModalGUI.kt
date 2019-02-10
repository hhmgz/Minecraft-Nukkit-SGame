package cn.hhm.mc.game.base.gui.window.lambda.defaults

import cn.hhm.mc.game.base.gui.function.Variable
import cn.hhm.mc.game.base.player.NukkitPlayer
import net.mcpes.hhm.nk.bedwars.gui.window.lambda.LambdaModalGUI

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
class LambdaVariableModalGUI(id: String, title: String, content: String) : LambdaModalGUI(id, title, content), Variable {
    override fun open(player: NukkitPlayer, params: Array<out Any>): Int {
        var f = this.content
        params.forEachIndexed { index, s -> f = f.replace("%$index", s.toString()) }
        return player.showGUI(this.data.replace(this.content, f), this)
    }
}