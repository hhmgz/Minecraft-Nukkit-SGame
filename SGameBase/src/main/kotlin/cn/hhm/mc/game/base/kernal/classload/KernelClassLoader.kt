package cn.hhm.mc.game.base.kernal.classload

import net.mcpes.hhm.kernel.EncryptedPluginClassLoader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.URLClassLoader
import java.nio.charset.Charset
import java.util.*


/**
 * SKernel
 *
 * @author hhm Copyright (c) 2018/8/23
 * version 1.0
 */
class KernelClassLoader(private val key: String, parent: ClassLoader, path: File) : URLClassLoader(arrayOf(path.toURI().toURL()), parent) {
    private fun decode(stream: InputStream, key: String): ByteArray {
        val out2 = ByteArrayOutputStream()
        val buffer = ByteArray(1024 * 4)
        var n2 = stream.read(buffer)
        while (n2 != -1) {
            out2.write(buffer, 0, n2)
            n2 = stream.read(buffer)
        }
        val n = key.toByteArray(Charset.forName("UTF-8"))
        val b = out2.toByteArray()!!
        val out = ByteArrayOutputStream()
        var i = 0
        b.forEach {
            out.write(it.toInt() xor 0xff xor n[i % (n.size - 1)].toInt())
            i++
        }
        return out.toByteArray()!!
    }

    override fun findClass(name: String): Class<*> {
        if (classes.containsKey(name)) return classes[name]!!
        try {
            /* 要创建的Class对象 */
            var clasz: Class<*>?
            /* 如果类已经在系统缓冲之中，不必再次装入它 */
            clasz = findLoadedClass(name)
            if (clasz != null) {
                EncryptedPluginClassLoader.setClass(name, clasz)
                return clasz
            }
            try {
                /* 读取经过加密的类文件 */
                val classData = this.getResourceAsStream(name.replace('.', '/'))
                if (classData != null) {
                    /* 解密 */
                    val decryptedClassData = decode(classData, key)
                    clasz = defineClass(name, decryptedClassData, 0, decryptedClassData.size)
                    resolveClass(clasz)
                    EncryptedPluginClassLoader.setClass(name, clasz)
                    return clasz
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            /* 如有必要，则装入相关的类 */
        } catch (gse: Exception) {
            throw ClassNotFoundException(gse.toString())
        }
        return super.findClass(name)
    }

    companion object {
        val classes = HashMap<String, Class<*>>()
    }
}