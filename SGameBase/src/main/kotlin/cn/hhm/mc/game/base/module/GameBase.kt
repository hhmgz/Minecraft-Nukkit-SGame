package cn.hhm.mc.game.base.module

import cn.hhm.mc.game.base.data.GameTipData
import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.utils.Games
import java.io.File

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 23:04
 * version 1.0
 */
class GameBase(val type: Games, val pluginName: String, file: File) : AbstractModule(file) {
    lateinit var gameInstance: Class<GameInstance>
    lateinit var tipData: GameTipData

    override fun onLoad() {
        tipData = GameTipData(this)
    }
}