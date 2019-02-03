package cn.hhm.mc.game.base.data

import cn.hhm.mc.game.base.module.GameBase
import cn.hhm.mc.game.base.utils.Games
import cn.hhm.mc.game.base.utils.config.Config

class GameTipData(val game: GameBase) {
    var config: Config? = null
    val data: HashMap<String, String> = hashMapOf()

    fun translate(key: String, params: Array<out Any>): String {
        var origin = this@GameTipData.data[key] ?: "NotFound"
        params.forEachIndexed { index, s ->
            origin = origin.replace("%" + (index + 1), s.toString())
        }
        return origin.replace("%GAME_MAIN_TITLE", game.type.mainTitle)
    }

    fun load() {
        this.configure(GameTipData.loadFromFile(this))
    }

    fun reload() {
        this.data.clear()
        if (this.config == null) {
            this.load()
        } else {
            this.config!!.reload()
            this.configure(this.config!!.getAll())
        }
    }

    fun configure(data: MutableMap<String, Any>) {
        data.forEach { t, u ->
            this.data[t] = u.toString()
        }
    }

    init {
        this.load()
    }

    companion object {
        val tipData: HashMap<Games, GameTipData> = hashMapOf()

        fun addTipData(game: Games, data: GameTipData) {
            tipData[game] = data
        }

        fun loadFromFile(data: GameTipData): MutableMap<String, Any> {
            val conf = Config(data.game.absolutePath + "/tips.yml")
            data.config = conf
            return conf.getAll()
        }
    }
}
