package cn.hhm.mc.game.base.utils

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.module.GameBase

class Games(val id: Int = SGameBase.gameTypes.size + 1, val codeName: String, val name: String, var coreConfig: GameCoreConfig, val instance: GameBase, val mainTitle: String) {
    init {
        SGameBase.gameTypes.add(this)
    }
}