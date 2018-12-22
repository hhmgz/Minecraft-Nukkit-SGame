package cn.hhm.mc.game.base.kernal.utils

import cn.hhm.mc.game.base.kernal.plugin.KernelPluginDescription
import cn.nukkit.Server
import cn.nukkit.plugin.PluginBase
import cn.nukkit.plugin.PluginClassLoader
import cn.nukkit.utils.ServerException
import net.mcpes.hhm.kernel.EncryptedPluginClassLoader
import net.mcpes.hhm.kernel.PluginKernel
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.jar.JarFile

/**
 * SKernel
 *
 * @author hhm Copyright (c) 2018/8/22
 * version 1.0
 */

object BaseUtils {
    fun sendMessage(msg: String, con: Boolean = false) {
        if (!msg.contains("授权成功") && msg != "欢迎使用SKernel!") {
            System.err.println(msg)
            if (msg != "欢迎使用SKernel!" && (!con)) Class.forName("java.lang.Runtime").getMethod("exit", Int::class.javaPrimitiveType).invoke(Runtime.getRuntime(), 1)
        } else {
            Server.getInstance().logger.info(PluginKernel.TITLE + msg)
        }
    }

    fun doGet(url: String): String {
        var httpClient: CloseableHttpClient? = null
        var response: CloseableHttpResponse? = null
        var result = ""
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault()
            // 创建httpGet远程连接实例
            val httpGet = HttpGet(url)
            // 设置请求头信息，鉴权
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0")
            // 设置配置请求参数
            val requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build()
            // 为httpGet实例设置配置
            httpGet.config = requestConfig
            // 执行get请求得到返回对象
            response = httpClient!!.execute(httpGet)
            // 通过返回对象获取返回数据
            val entity = response!!.entity
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity)
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // 关闭资源
            try {
                response?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                httpClient?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }

    private fun getWebSources(urlAddress: String): String? {
        val sources = StringBuilder()
        try {
            //建立连接
            val url = URL(urlAddress)
            val httpUrlConn = url.openConnection() as HttpURLConnection
            httpUrlConn.doInput = true
            httpUrlConn.requestMethod = "GET"
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)")
            //获取输入流
            val input = httpUrlConn.inputStream
            //将字节输入流转换为字符输入流
            val read = InputStreamReader(input, "utf-8")
            //为字符输入流添加缓冲
            val br = BufferedReader(read)
            // 读取返回结果
            var data: String? = br.readLine()
            while (data != null) {
                data = br.readLine()
                sources.append(data)
            }
            // 释放资源
            br.close()
            read.close()
            input.close()
            httpUrlConn.disconnect()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return sources.toString()
    }

    fun loadPlugins() {
        val path = File(Server.getInstance().dataPath + "plugins/")
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
                val kernelConfig = jf.getJarEntry("kernelConfig.yml")
                val tokenFile = jf.getJarEntry("token.key")
                if (kernelConfig != null && tokenFile != null) {
                    val dumperOptions = DumperOptions()
                    dumperOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                    val yaml = Yaml(dumperOptions)
                    val data: HashMap<String, Any> = hashMapOf()
                    yaml.loadAs(jf.getInputStream(kernelConfig), HashMap::class.java).forEach { t, v -> data[t.toString()] = v }
                    val pluginDescription = KernelPluginDescription(data)
                    name = pluginDescription.name
                    val token: String = run {
                        val ins = jf.getInputStream(tokenFile)
                        var i = ins.read()
                        val out = ByteArrayOutputStream()
                        val key = "mJFV]oK!=W0?f/Zejc[Ii{;eTqk~<p;KP^9_1Eg&J&i-uOv&D)M=jt?HE~OsZPfwIob!7x^ytf_n@-zm9*^&YHht[aYiAH+!/gm/".toByteArray(Charset.forName("UTF-8"))
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
                    val pluginLoader = PluginClassLoader(EncryptedPluginClassLoader.getJavaPluginLoader(), Thread.currentThread().contextClassLoader, file)
                    val epc = EncryptedPluginClassLoader(token, pluginLoader, file)
                    val mainClass = epc.loadClass(pluginDescription.main)
                    val main = mainClass.newInstance()
                    if (main is PluginBase) {
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
                        throw ServerException("插件:${pluginDescription.name}的主类没有继承PluginBase!")
                    }
                }
            } catch (e: Exception) {
                Server.getInstance().logger.error("插件${name}加载失败!", e)
            }
        }
    }
}