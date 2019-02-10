package cn.hhm.mc.game.base.data

import cn.nukkit.plugin.PluginBase
import cn.nukkit.utils.Config
import java.io.File
import java.util.jar.JarFile

infix fun String.translate(params: Array<out Any>): String {
    return GameTipData.translate(this, params)
}

object GameTipData {
    var configMap: HashMap<PluginBase, Config> = hashMapOf()
    val data: HashMap<String, String> = hashMapOf()

    fun translate(key: String, params: Array<out Any>): String {
        var origin = this.data[key] ?: "NotFound"
        params.forEachIndexed { index, s ->
            origin = origin.replace("%" + (index + 1), s.toString())
        }
        return origin
    }

    fun load(plugin: PluginBase, title: String) {
        val config = Config(Config.YAML)
        val f = PluginBase::class.java.getDeclaredField("file")
        f.isAccessible = true
        val file = f.get(plugin) as File
        val jar = JarFile(file)
        config.load(jar.getInputStream(jar.getEntry("tips.yml")))
        configMap[plugin] = config
        config.all.forEach { t, u -> data[t] = u.toString().replace("%TITLE", title) }
    }
}
