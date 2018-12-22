package net.mcpes.hhm.kernel

import cn.hhm.mc.game.base.kernal.classload.KernelClassLoader
import cn.hhm.mc.game.base.kernal.utils.BaseUtils
import cn.hhm.mc.game.base.module.AbstractModule
import cn.nukkit.plugin.PluginBase
import java.io.File

/**
 * SKernel
 *
 * @author hhm Copyright (c) 2018/8/23
 * version 1.0
 */
class PluginKernel(file: File) : AbstractModule(file) {
    lateinit var cl: KernelClassLoader
    private lateinit var base: PluginBase
    lateinit var ksc: Class<*>
    lateinit var kso: Any

    override fun onLoad() {
        try {
            this.cl = KernelClassLoader("V:WuI6LG64j8NcSk4/zJpS[58z<NOdv5X'^2,?\"oiPa0BePc\"/-1T,!qvd}XL5qCnzYwuil5vLfo)k_geMEC`!SLC/k2m&*8^.hi7-:d`-/(,B79;HYC'K9O9|*-!nO1L^_)v/yG5#W4-xy&}W0p4u/48e:3ltx)avNNW[/qO)?a7rwN9j2w0,k>j{:SR#qtIphF3|q", PluginKernel::class.java.classLoader, this.file)
            BaseUtils.loadPlugins()
            this.ksc = cl.loadClass("cn.hhm.mc.game.base.kernal.KernelSystem")
            this.kso = ksc.newInstance()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onDisable() {
        this.base.onDisable()
    }

    fun init(id: Int, plugin: PluginBase) {
        this.ksc.getMethod("init", Int::class.javaPrimitiveType, PluginBase::class.java).invoke(this.kso, id, plugin)
    }

    fun tokenCheck(id: Int, version: String, token: String) {
        this.ksc.getMethod("tokenCheck", Int::class.javaPrimitiveType, String::class.java, String::class.java).invoke(this.kso, id, version, token)
    }

    companion object {
        const val TITLE = "§l§e[§6S§eK§ce§ar§dn§2e§3l§e] §e"
    }
}