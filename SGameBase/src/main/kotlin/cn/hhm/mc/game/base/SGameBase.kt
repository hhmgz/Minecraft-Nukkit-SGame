package cn.hhm.mc.game.base

import cn.nukkit.plugin.PluginBase
import net.mcpes.hhm.kernel.PluginKernel

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 22:14
 * version 1.0
 */
class SGameBase : PluginBase() {
    lateinit var kernel: PluginKernel

    override fun onLoad() {
        kernel = PluginKernel(this.file)
        kernel.onLoad()
    }

    override fun onEnable() {
        kernel.onEnable()
    }

    override fun onDisable() {
        kernel.onDisable()
    }

    init {
        instance = this
    }

    companion object {
        lateinit var instance: SGameBase
    }
}