package cn.hhm.mc.game.base

import cn.hhm.mc.game.base.config.MasterConfig
import cn.hhm.mc.game.base.config.VipConfig
import cn.hhm.mc.game.base.data.GameTipData
import cn.hhm.mc.game.base.gui.listener.GUIListener
import cn.hhm.mc.game.base.listener.FloatingTextListener
import cn.hhm.mc.game.base.listener.NormalListener
import cn.hhm.mc.game.base.module.GameBase
import cn.hhm.mc.game.base.module.ModuleLoader
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.NukkitBugFixer
import cn.nukkit.plugin.PluginBase
/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 22:14
 * version 1.0
 */
class SGameBase : PluginBase() {
    lateinit var masterConfig: MasterConfig

    override fun onLoad() {
        NukkitBugFixer.fix()
        masterConfig = MasterConfig()
        masterConfig.load()
        masterConfig.save()
        VipConfig.init()
        GameTipData.load(this, TITLE)
        ModuleLoader.loadModules()
    }

    override fun onEnable() {
        this.registerListeners()
        info("启动成功")
    }

    override fun onDisable() {
    }

    private fun registerListeners(){
        this.server.pluginManager.registerEvents(NormalListener(),this)
        this.server.pluginManager.registerEvents(GUIListener(),this)
        this.server.pluginManager.registerEvents(FloatingTextListener(), this)
    }

    init {
        instance = this
    }

    companion object {
        const val TITLE = ""
        lateinit var instance: SGameBase
        val gameTypes: ArrayList<Games> = arrayListOf()
        val gameModules: HashMap<Games,GameBase> = hashMapOf()

        fun info(msg:String){
            instance.logger.info(msg)
        }
    }
}