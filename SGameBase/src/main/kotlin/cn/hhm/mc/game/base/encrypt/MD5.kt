package cn.hhm.mc.game.base.encrypt

import java.nio.charset.Charset
import java.security.MessageDigest

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/29
 * version 1.0
 */
object MD5 {
    /**
     * 获取MD5-32位加密<br></br>
     *
     * @param str String 内容<br></br>
     * @return String md5
     */
    @JvmStatic
    fun encrypt(str: String): String {
        val s: String
        val hexDigist = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val md = MessageDigest.getInstance("MD5")
        md.update(str.toByteArray(Charset.forName("UTF-8")))
        val datas = md.digest() //16个字节的长整数
        val sr = CharArray(2 * 16)
        var k = 0
        for (i in 0..15) {
            val b = datas[i].toInt()
            sr[k++] = hexDigist[b.ushr(4) and 0xf]//高4位
            sr[k++] = hexDigist[b and 0xf]//低4位
        }
        s = String(sr)
        return s
    }
}