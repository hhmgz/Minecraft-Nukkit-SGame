package cn.hhm.mc.game.base

import cn.hhm.mc.game.base.gui.listener.GUIListener
import cn.hhm.mc.game.base.listener.NormalListener
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.NukkitBugFixer
import cn.nukkit.plugin.PluginBase
import cn.hhm.mc.game.base.kernal.PluginKernel
import cn.hhm.mc.game.base.module.GameBase

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 22:14
 * version 1.0
 */
class SGameBase : PluginBase() {
    lateinit var kernel: PluginKernel

    override fun onLoad() {
        NukkitBugFixer.fix()
        kernel = PluginKernel(this.file)
        kernel.onLoad()
    }

    override fun onEnable() {
        this.registerListeners()
        kernel.onEnable()
        this.loadGames()
        info("启动成功")
    }

    override fun onDisable() {
        kernel.onDisable()
    }

    private fun loadGames(){
    }

    private fun registerListeners(){
        this.server.pluginManager.registerEvents(NormalListener(),this)
        this.server.pluginManager.registerEvents(GUIListener(),this)
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: SGameBase
        val gameTypes: ArrayList<Games> = arrayListOf()
        val gameModules: HashMap<Games,GameBase> = hashMapOf()

        fun info(msg:String){
            instance.logger.info(msg)
        }
    }
}