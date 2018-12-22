package cn.hhm.mc.game.base.kernal

import cn.hhm.mc.game.base.encrypt.AES
import cn.hhm.mc.game.base.kernal.utils.BaseUtils
import cn.hhm.mc.game.base.kernal.utils.BaseUtils.doGet
import cn.hhm.mc.game.base.kernal.utils.BaseUtils.sendMessage
import cn.nukkit.plugin.PluginBase
import net.sf.json.JSONObject

/**
 * SKernel
 *
 * @author hhm Copyright (c) 2018/8/22
 * version 1.0
 */
class KernelSystem {
    val plugins: HashMap<Int, PluginBase> = hashMapOf()

    fun init(id: Int, plugin: PluginBase) {
        if (plugins[id]!!.name.contains("新版本提示:插件") || plugin.name.contains("欢迎使用SKernel!") || plugin.name.contains("授权成功")) {
            sendMessage("插件名称包含违禁词!")
            Class.forName("java.lang.System").getMethod("exit", Int::class.javaPrimitiveType).invoke(null, -1)
        }
        plugins[id] = plugin
    }

    fun tokenCheck(id: Int, version: String, token: String) {
        try {
            val data = JSONObject.fromObject(AES.decrypt(JSONObject.fromObject(doGet("http://www.mcpes.net/api/pluginInfo"))["$id"]!!.toString(), ":XSawR]15<8n~=6dvT4uJ-/.,#(z$.y"))
            val tokens = data["token"] as JSONObject
            if (tokens[version] != token) {
                BaseUtils.sendMessage("插件${plugins[id]!!.name}Token错误!")
                Class.forName("java.lang.System").getMethod("exit", Int::class.javaPrimitiveType).invoke(null, -1)
                return
            }
            sendMessage("§e插件${plugins[id]!!.name}加载成功!")
        } catch (e: Exception) {
            e.printStackTrace()
            sendMessage("数据解析失败!")
        }
    }
}