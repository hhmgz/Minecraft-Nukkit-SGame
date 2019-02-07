package cn.hhm.mc.game.base.utils

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.module.GameBase

class Games(val codeName: String, val name: String, var coreConfig: GameCoreConfig, val mainTitle: String, val id: Int = SGameBase.gameTypes.size + 1) {
    init {
        SGameBase.gameTypes.add(this)
    }

    lateinit var instance: GameBase
}