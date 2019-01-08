package cn.hhm.mc.game.base.utils

import cn.hhm.mc.game.base.SGameBase

class Games(val id:Int = SGameBase.gameTypes.size+1, val codeName:String, val name:String,var coreConfig: GameCoreConfig) {
    init {
        SGameBase.gameTypes.add(this)
    }
}