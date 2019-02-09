package cn.hhm.mc.game.base

import cn.hhm.mc.game.base.config.MasterConfig
import cn.hhm.mc.game.base.config.VipConfig
import cn.hhm.mc.game.base.gui.listener.GUIListener
import cn.hhm.mc.game.base.kernel.PluginKernel
import cn.hhm.mc.game.base.listener.FloatingTextListener
import cn.hhm.mc.game.base.listener.NormalListener
import cn.hhm.mc.game.base.module.GameBase
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.NukkitBugFixer
import cn.hhm.mc.game.base.utils.scoreboard.ScoreboardAPI
import cn.nukkit.Server
import cn.nukkit.plugin.PluginBase
import java.io.File

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 22:14
 * version 1.0
 */
class SGameBase : PluginBase() {
    lateinit var kernel: PluginKernel
    lateinit var scoreboardAPI: ScoreboardAPI
    lateinit var masterConfig: MasterConfig

    override fun onLoad() {
        NukkitBugFixer.fix()
        masterConfig = MasterConfig()
        masterConfig.load()
        masterConfig.save()
        VipConfig.init()
        this.loadModules()
    }

    override fun onEnable() {
        this.registerListeners()
        kernel.onEnable()
        scoreboardAPI.onEnable()
        info("启动成功")
    }

    override fun onDisable() {
        kernel.onDisable()
        scoreboardAPI.onDisable()
    }

    private fun registerListeners(){
        this.server.pluginManager.registerEvents(NormalListener(),this)
        this.server.pluginManager.registerEvents(GUIListener(),this)
        this.server.pluginManager.registerEvents(FloatingTextListener(), this)
    }

    fun loadModules() {
        kernel = PluginKernel(this.file)
        kernel.onLoad()
        scoreboardAPI = ScoreboardAPI(File(Server.getInstance().pluginPath + "/"))
        scoreboardAPI.onLoad()
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