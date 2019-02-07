package cn.hhm.mc.game.base.config

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.utils.config.Config
import java.util.*

/**
 * @author hhm @ SBedWars Project
 */
object GradeConfig {
    lateinit var config: Config
        private set
    var levelUpNeed: HashMap<Int, Int> = hashMapOf()
        private set
    var max: Int = 0
        private set

    fun init() {
        config = Config(SGameBase.instance.dataFolder.toString() + "/grades.yml", Config.ConfigType.YAML)
        init1()
        max = config.getInt("最大等级")
        levelUpNeed = HashMap()
        val levelUp = config["升级所需"] as HashMap<*, *>
        for (key in levelUp.keys) {
            levelUpNeed[key.toString().toInt()] = levelUp[key]!!.toString().toInt()
        }
        levelUpNeed[max] = Integer.MAX_VALUE
    }

    private fun init1() {
        if (config.content.isEmpty()) {
            val map = LinkedHashMap<String, Any>()
            map["最大等级"] = 100
            val levelUp = HashMap<Int, Int>()
            for (i in 1..99) {
                levelUp[i] = i * 100
            }
            map["升级所需"] = levelUp
            config.content = map
            config.save()
        }
    }
}
