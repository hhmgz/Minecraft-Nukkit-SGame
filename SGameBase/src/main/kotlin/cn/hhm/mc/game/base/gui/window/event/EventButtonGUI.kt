package cn.hhm.mc.game.base.gui.window.event

import cn.hhm.mc.game.base.gui.window.ButtonGUI
import cn.hhm.mc.game.base.gui.window.EventGUI
import cn.nukkit.form.window.FormWindowSimple

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/26
 * version 1.0
 */
open class EventButtonGUI(id: String, title: String, content: String) : ButtonGUI(ProcessMode.EVENT, id, title, content), EventGUI {
    override fun analysis(data: String): HashMap<String, Any> {
        val map: HashMap<String, Any> = HashMap()
        if (gui !is FormWindowSimple) return map
        val buttonID = data.toInt()
        map["id"] = buttonID
        if (buttonID < this.gui.buttons.size) {
            map["button"] = this.gui.buttons[buttonID]
        }
        return map
    }
}