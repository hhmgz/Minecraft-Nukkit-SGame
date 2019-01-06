package cn.hhm.mc.game.base.gui.event

import cn.hhm.mc.game.base.gui.NukkitGraphicalUserInterface
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.nukkit.event.Event

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/11
 * version 1.0
 *
 * EventGUI 当收到回复后将触发此事件
 *
 * @param player 玩家
 * @param gui GUI对象
 * @param closed 是否直接关闭窗口
 * @param data 表单信息
 */
class PlayerGUIResultEvent(val player: NukkitPlayer, val gui: NukkitGraphicalUserInterface, val closed: Boolean, val data: HashMap<String, Any> = HashMap()) : Event()