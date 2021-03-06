package cn.hhm.mc.game.bedwars

import cn.hhm.mc.game.base.config.ConfigStorageMode
import cn.hhm.mc.game.base.data.GameTipData
import cn.hhm.mc.game.base.module.EncryptedPlugin
import cn.hhm.mc.game.base.module.GameBase
import cn.hhm.mc.game.base.utils.GameCoreConfig
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.bedwars.gui.configure.BedWarsConfigureGUI
import cn.hhm.mc.game.bedwars.gui.shop.BedWarsGUIShopBase
import cn.nukkit.item.Item

class SBedWars : GameBase(Games("bw", "BedWars", GameCoreConfig(ConfigStorageMode.FILE), TITLE)), EncryptedPlugin {
    override fun onLoad() {
        GameTipData.load(this, TITLE)
        this.info("加载完成")
    }

    override fun onEnable() {
        BedWarsGUIShopBase.init()
        BedWarsConfigureGUI.init()
        this.loadRooms()
        this.info("启动成功")
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