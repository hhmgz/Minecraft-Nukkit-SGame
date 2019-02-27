package cn.hhm.mc.game.base.gui.window.lambda

import cn.hhm.mc.game.base.gui.window.LambdaGUI
import cn.hhm.mc.game.base.gui.window.ModalGUI
import cn.hhm.mc.game.base.player.NukkitPlayer
import cn.nukkit.Player
import cn.nukkit.form.window.FormWindowModal
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
open class LambdaModalGUI(id: String, title: String, content: String) : ModalGUI(ProcessMode.LAMBDA, id, title, content), LambdaGUI {
    var buttonClickedListener: BiConsumer<Boolean, Player>? = null
    var closedClickedListener: Consumer<Player>? = null

    override fun callClicked(player: NukkitPlayer, data: String) {
        if (gui !is FormWindowModal) return
        if (buttonClickedListener == null) return
        if (data == "null") return
        this.buttonClickedListener!!.accept(data.toBoolean(), player)
    }

    override fun callClosed(player: NukkitPlayer) {
        if (gui !is FormWindowModal) return
        if (closedClickedListener == null) return
        this.closedClickedListener!!.accept(player)
    }
}