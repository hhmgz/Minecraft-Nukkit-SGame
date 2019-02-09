package cn.hhm.mc.game.base.utils

import cn.nukkit.Server
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.concurrent.FutureCallback
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.util.EntityUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.function.Consumer

object MineCraftRMB {
    var sid = "Undefined"
    var key = "Undefined"

    /**
     * 获取MD5-32位加密<br></br>
     *
     * @param s String 内容<br></br>
     * @return String md5
     */
    fun getMD5(s: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(s.toByteArray())
            val b = md.digest()
            var i: Int
            val sb = StringBuilder()
            for (aB in b) {
                i = aB.toInt()
                if (i < 0)
                    i += 256
                if (i < 16)
                    sb.append("0")
                sb.append(Integer.toHexString(i))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

    }

    fun doGet(url: String, c: Consumer<String>) {
        val httpPost = HttpPost(url)
        val nvps = ArrayList<NameValuePair>()
        httpPost.setHeader("accept", "*/*")
        httpPost.setHeader("connection", "Keep-Alive")
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 Edge/15.16199 java")
        httpPost.entity = UrlEncodedFormEntity(nvps)
        // 传入HttpPost request
        val httpclient = HttpAsyncClients.createDefault()
        httpclient.start()
        httpclient.execute(httpPost, object : FutureCallback<HttpResponse> {
            override fun cancelled() {
                Server.getInstance().logger.warning("发送GET请求被取消！")
            }

            override fun failed(e: Exception) {
                Server.getInstance().logger.warning("发送GET请求出现异常！$e")
            }

            override fun completed(r: HttpResponse) {
                val s = EntityUtils.toString(r.entity)
                c.accept(s)
                httpclient.close()
            }
        })
    }

    /**
     * MCRMB-查询玩家点卷数量<br></br>
     *
     * @param name string 玩家名字<br></br>
     */
    fun getMoney(name: String, c: Consumer<String>) {
        val sign = getMD5("$sid$name$key")
        doGet("http://www.mcrmb.com/Api/CheckMoney?sign=$sign&sid=$sid&wname=$name", c)
    }

    /**
     * MCRMB-添加玩家点卷<br></br>
     *
     * @param name  string 玩家名字<br></br>
     * @param money int 点卷数量<br></br>
     * @param reason string 用途<br></br>
     */
    fun addMoney(name: String, money: Int, reason: String, c: Consumer<String>) {
        val sign = getMD5(sid + name + "1" + reason + money + key)
        doGet("http://www.mcrmb.com/Api/Manual?sign=$sign&sid=$sid&wname=$name&type=1&text=$reason&money=$money", c)
    }

    /**
     * MCRMB-减少玩家点卷<br></br>
     *
     * @param name  string 玩家名字<br></br>
     * @param money int 点卷数量<br></br>
     * @param reason string 用途<br></br>
     * @return int 1:成功,-1:发生错误<br></br>
     */
    fun delMoney(name: String, money: Int, reason: String, c: Consumer<String>) {
        val sign = getMD5(sid + name + "2" + reason + money + key)
        doGet("http://www.mcrmb.com/Api/Manual?sign=$sign&sid=$sid&wname=$name&type=2&text=$reason&money=$money", c)
    }

    /**
     * MCRMB-设置玩家点卷<br></br>
     *
     * @param name  string 玩家名字<br></br>
     * @param money int 点卷数量<br></br>
     * @param reason string 用途<br></br>
     * @return int 1:增加,2:减少,3:无变化,-1:发生错误<br></br>
     */
    fun setMoney(name: String, money: Int, reason: String, c: Consumer<String>) {
        val sign = getMD5(sid + name + "3" + reason + money + key)
        doGet("http://www.mcrmb.com/Api/Manual?sign=$sign&sid=$sid&wname=$name&type=3&text=$reason&money=$money", c)
    }

    /**
     * MCRMB-玩家支出点卷<br></br>
     *
     * @param name  string 玩家名字<br></br>
     * @param money int 点卷数量<br></br>
     * @param reason string 用途<br></br>
     */
    fun pay(name: String, money: Int, reason: String, c: Consumer<String>) {
        val sign = getMD5(sid + name + reason + +money + key)
        doGet("http://www.mcrmb.com/Api/Pay?sign=$sign&sid=$sid&wname=$name&use=$reason&money=$money", c)
    }
}