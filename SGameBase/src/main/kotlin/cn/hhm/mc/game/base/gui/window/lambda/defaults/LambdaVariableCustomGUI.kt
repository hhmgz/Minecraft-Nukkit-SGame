package cn.hhm.mc.game.base.gui.window.lambda.defaults

import cn.hhm.mc.game.base.gui.function.Variable
import cn.hhm.mc.game.base.gui.window.lambda.LambdaCustomGUI
import cn.hhm.mc.game.base.utils.NukkitPlayer

class LambdaVariableCustomGUI(id: String, title: String, imageURL: String = "") : LambdaCustomGUI(id, title, imageURL), Variable {
    override fun open(player: NukkitPlayer, params: Array<out Any>): Int {
        var d = this.data
        params.forEachIndexed { index, s -> d = d.replace("%$index", s.toString()) }
        return player.showGUI(d, this)
    }
}