package cn.hhm.mc.game.base.module.classload

import cn.nukkit.Server
import cn.nukkit.plugin.JavaPluginLoader
import cn.nukkit.plugin.PluginLoader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URLClassLoader
import java.nio.charset.Charset

/**
 * SKernel
 *
 * @author hhm Copyright (c) 2018/8/25
 * version 1.0
 */
class EncryptedPluginClassLoader(private val token: String, parent: ClassLoader, path: File) : URLClassLoader(arrayOf(path.toURI().toURL()), parent) {
    private fun decode(stream: InputStream): ByteArray {
        val out2 = ByteArrayOutputStream()
        val buffer = ByteArray(1024 * 2)
        var n2 = stream.read(buffer)
        while (n2 != -1) {
            out2.write(buffer, 0, n2)
            n2 = stream.read(buffer)
        }
        val n = token.toByteArray(Charset.forName("UTF-8"))
        val b = out2.toByteArray()!!
        val out = ByteArrayOutputStream()
        var i = 0
        b.forEach {
            out.write(it.toInt() xor 0xac xor 0xff xor n[i % (n.size - 1)].toInt())
            i++
        }
        return out.toByteArray()!!
    }

    override fun findClass(name: String): Class<*> {
        if (KernelClassLoader.classes.containsKey(name)) return KernelClassLoader.classes[name]!!
        try {
            var clasz: Class<*>?
            clasz = findLoadedClass(name)
            if (clasz != null) {
                setClass(name, clasz)
                return clasz
            }
            try {
                val classData = this.getResourceAsStream(name.replace('.', '/') + ".pe")
                if (classData != null) {
                    /* 解密 */
                    val decryptedClassData = decode(classData)
                    clasz = defineClass(name, decryptedClassData, 0, decryptedClassData.size)
                    resolveClass(clasz)
                    setClass(name, clasz)
                    return clasz
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (gse: Exception) {
            gse.printStackTrace()
        }
        return super.findClass(name)
    }

    companion object {
        private var javaPluginLoader: JavaPluginLoader? = null
        private var nkClasses: HashMap<String, Class<*>>? = null
        @Throws(IllegalAccessException::class, NoSuchFieldException::class)
        fun getValue(instance: Any, fieldName: String): Any {
            val field = instance.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true // 参数值为true，禁止访问控制检查
            return field.get(instance)
        }

        fun getJavaPluginLoader(): JavaPluginLoader {
            if (javaPluginLoader == null) {
                val fileAssociations = getValue(Server.getInstance().pluginManager, "fileAssociations") as Map<String, PluginLoader>
                var pl: PluginLoader? = null
                fileAssociations.values.stream().forEach {
                    pl = it
                    return@forEach
                }
                javaPluginLoader = pl!! as JavaPluginLoader
            }
            return javaPluginLoader!!
        }

        private fun getNukkitClasses(): HashMap<String, Class<*>> {
            if (nkClasses == null) {
                val classes = getValue(getJavaPluginLoader(), "classes") as HashMap<String, Class<*>>
                nkClasses = classes
            }
            return nkClasses!!
        }

        fun setClass(name: String, clasz: Class<*>) {
            getNukkitClasses()[name] = clasz
        }
    }
}