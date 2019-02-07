package cn.hhm.mc.game.base.config

import cn.hhm.mc.game.base.SGameBase
import cn.nukkit.utils.Config
import java.io.File
import java.util.*

/**
 * @author hhm @ SBedWars Project
 */
object VipConfig {
    lateinit var file: File
        private set
    lateinit var config: Config
        private set
    private val nowTimeStamp: Long
        get() = System.currentTimeMillis()

    fun init() {
        file = File(SGameBase.instance.dataFolder.toString() + "/vips.yml")
        config = Config(file, 2)
    }

    fun exists(name: String): Boolean {
        return config.all[name] != null
    }

    fun setVip(name: String, day: Int): Long {
        val cal = Calendar.getInstance()
        cal.time = Date(config.getLong(name))
        cal.add(Calendar.DAY_OF_MONTH, day)
        val t = cal.time.time
        config.set(name, t)
        config.save()
        return t
    }

    fun addVip(name: String, day: Int): Long {
        return if (isVip(name)) {
            val cal = Calendar.getInstance()
            cal.time = Date(config.getLong(name))
            cal.add(Calendar.DAY_OF_MONTH, day)
            val t = cal.time.time
            config.set(name, t)
            config.save()
            t
        } else {
            setVip(name, day)
        }
    }

    fun delVip(name: String, day: Int) {
        if (!exists(name)) return
        val remove = (config.getLong(name) - (day * 24 * 60 * 60 * 1000))
        if (remove - nowTimeStamp > 100) {
            config.set(name, remove + nowTimeStamp)
            config.save()
            return
        }
        config.remove(name)
        config.save()
    }

    fun delVip(name: String) {
        config.remove(name)
        config.save()
    }

    fun isVip(name: String): Boolean {
        return exists(name) && config.getLong(name, java.lang.Long.MIN_VALUE) > nowTimeStamp
    }
}
