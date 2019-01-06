package cn.hhm.mc.game.base.gui.window.lambda.defaults

import cn.hhm.mc.game.base.gui.function.Variable
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.nukkit.form.element.ElementButtonImageData


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 *
 * Lambada型GUI模板
 * 模板:可变提示信息
 */
class LambdaVariableTipGUI(id: String, tipText: String, title: String = "提示 | Announcement", imageData: ElementButtonImageData? = null) : LambdaTipGUI(id, tipText, title, imageData), Variable {
    override fun open(player: NukkitPlayer, params: Array<out Any>): Int {
        var f = this.tipText
        params.forEachIndexed { index, s -> f = f.replace("%$index", s.toString()) }
        return player.showGUI(this.data.replace(this.tipText, f), this)
    }
}