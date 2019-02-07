package cn.hhm.mc.game.bedwars

import cn.hhm.mc.game.base.config.ConfigStorageMode
import cn.hhm.mc.game.base.module.GameBase
import cn.hhm.mc.game.base.utils.GameCoreConfig
import cn.hhm.mc.game.base.utils.Games
import cn.nukkit.item.Item
import java.io.File

class SBedWars(file: File) : GameBase(Games("bw", "BedWars", GameCoreConfig(ConfigStorageMode.FILE), TITLE), "SBedWars", file) {
    override fun onLoad() {
        super.onLoad()
        this.info("加载完成")
    }

    override fun onEnable() {
        this.loadRooms()
    }

    override fun onDisable() {
        this.info("关闭成功")
    }

    init {
        type.instance = this
        instance = this
        gold.customName = "$TITLE§e金"
        silver.customName = "$TITLE§f银"
        copper.customName = "$TITLE§6铜"
        diamond.customName = "$TITLE§b钻石"
    }

    companion object {
        const val TITLE = ""
        lateinit var instance: SBedWars
        @JvmStatic
        val diamond: Item = Item.get(264, 0, 1)
        @JvmStatic
        val gold: Item = Item.get(266, 0, 1)
        @JvmStatic
        val silver: Item = Item.get(265, 0, 1)
        @JvmStatic
        val copper: Item = Item.get(336, 0, 1)
    }
}