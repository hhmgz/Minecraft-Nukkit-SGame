package cn.hhm.mc.game.base.module

import cn.hhm.mc.game.base.encrypt.AES
import cn.hhm.mc.game.base.module.classload.EncryptedPluginClassLoader
import cn.hhm.mc.game.base.module.plugin.ModuleDescription
import cn.nukkit.Server
import cn.nukkit.plugin.PluginBase
import cn.nukkit.plugin.PluginClassLoader
import cn.nukkit.utils.ServerException
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset
import java.util.jar.JarFile

object ModuleLoader {
    fun loadModules() {
        val path = File(Server.getInstance().dataPath + "/plugins/")
        val jarFiles = arrayListOf<File>()
        path.listFiles().forEach {
            if (it.isFile && it.absolutePath.toLowerCase().endsWith(".jar")) {
                jarFiles.add(it)
            }
        }
        var jf: JarFile
        var name = ""
        for (file in jarFiles) {
            try {
                jf = JarFile(file)
                val moduleInfo = jf.getJarEntry("sgModule.info")
                if (moduleInfo != null) {
                    val sgm: String = run {
                        val ins = jf.getInputStream(moduleInfo)
                        var i = ins.read()
                        val out = ByteArrayOutputStream()
                        val key = "RTYl2!D0KQ7enrVoQG8VUq1Nl%hc2jhO@%Oo3EoJWOZ7igtR*TSY^5l\$L6s\$".toByteArray(Charset.forName("UTF-8"))
                        var j = 0
                        while (i != -1) {
                            out.write(i xor 0xac xor 0xff xor key[j % (key.size - 1)].toInt())
                            i = ins.read()
                            j++
                        }
                        out.close()
                        ins.close()
                        String(out.toByteArray(), Charset.forName("UTF-8"))
                    }
                    val dumperOptions = DumperOptions()
                    dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                    val yaml = Yaml(dumperOptions)
                    val data: HashMap<String, Any> = hashMapOf()
                    yaml.loadAs(sgm, HashMap::class.java).forEach { t, v -> data[t.toString()] = v }
                    val pluginDescription = ModuleDescription(data)
                    name = pluginDescription.name
                    val token: String = AES.decrypt(pluginDescription.key!!, "uxYYFNg%HRnCimX3z\$s7YWdz6874&hB!d5U*qbzlmhS#&#&kv&qfPAs4o2Aq")
                    val pluginLoader = PluginClassLoader(EncryptedPluginClassLoader.getJavaPluginLoader(), Thread.currentThread().contextClassLoader, file)
                    val epc = EncryptedPluginClassLoader(token, pluginLoader, file)
                    val mainClass = epc.loadClass(pluginDescription.main)
                    val main = mainClass.newInstance()
                    if (main is PluginBase && main is EncryptedPlugin) {
                        jf.stream().forEach {
                            if (it.name.endsWith(".pe")) {
                                val cn = it.name.replace("\\", ".").replace("/", ".")
                                cn.substring(0, cn.length - 4)
                                epc.loadClass(cn)
                            }
                        }
                        val dataFolder = File(file.parentFile, pluginDescription.name)
                        if (dataFolder.exists() && !dataFolder.isDirectory) {
                            throw IllegalStateException("Projected dataFolder '" + dataFolder.toString() + "' for " + pluginDescription.name + " exists and is not a directory")
                        }
                        main.init(EncryptedPluginClassLoader.getJavaPluginLoader(), Server.getInstance(), pluginDescription, dataFolder, file)
                        main.onLoad()
                        Server.getInstance().pluginManager.plugins[pluginDescription.name] = main
                    } else {
                        throw ServerException("插件:${pluginDescription.name}的主类没有继承EncryptedClasses!")
                    }
                }
            } catch (e: Exception) {
                Server.getInstance().logger.error("插件${name}加载失败!", e)
            }
        }
    }
}