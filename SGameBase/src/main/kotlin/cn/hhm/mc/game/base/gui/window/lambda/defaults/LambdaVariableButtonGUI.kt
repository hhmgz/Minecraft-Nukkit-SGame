package cn.hhm.mc.game.base.gui.window.lambda.defaults

import cn.hhm.mc.game.base.gui.function.Variable
import cn.hhm.mc.game.base.gui.window.lambda.LambdaButtonGUI
import cn.hhm.mc.game.base.utils.NukkitPlayer

class LambdaVariableButtonGUI(id: String, title: String, content: String) : LambdaButtonGUI(id, title, content), Variable {
    override fun open(player: NukkitPlayer, params: Array<out Any>): Int {
        var f = this.content
        params.forEachIndexed { index, s -> f = f.replace("%$index", s.toString()) }
        return player.showGUI(this.data.replace(this.content, f), this)
    }
}