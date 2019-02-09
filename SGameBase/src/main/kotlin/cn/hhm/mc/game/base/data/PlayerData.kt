package cn.hhm.mc.game.base.data

import cn.hhm.mc.game.base.SGameBase
import cn.hhm.mc.game.base.SGameBase.Companion.TITLE
import cn.hhm.mc.game.base.config.GradeConfig
import cn.hhm.mc.game.base.config.VipConfig
import cn.hhm.mc.game.base.utils.NukkitPlayer
import cn.hhm.mc.game.base.utils.config.Config
import cn.nukkit.Server
import java.io.File

class PlayerData(val name: String) {
    val data: Config = Config(File(SGameBase.instance.dataFolder.absolutePath + "/players/$name.yml"), Config.ConfigType.YAML, linkedMapOf(
            "level" to 1,
            "exp" to 0,
            "usedTitle" to 0,
            "titles" to arrayListOf("§6『§a游§b戏§c萌§d新§5』")
    ))
    var level: Int = data["level"] as Int
    var exp = data["exp"] as Int
    var titles = arrayListOf("§6『§a游§b戏§c萌§d新§5』")
    var usedTitle = 0

    val isVip: Boolean
        get() = VipConfig.isVip(name)

    init {
        data.save()
    }

    fun addExp(exp: Int) {
        var now = exp
        if (this.isVip) now *= 2
        val player = Server.getInstance().getPlayerExact(name) as? NukkitPlayer ?: return
        if (this.level >= GradeConfig.max) {
            this.exp = 0
            this.reportLevel(player)
            return
        }
        var levelUp = this.level
        var addExp = this.exp + now
        while (levelUp < GradeConfig.max && addExp > GradeConfig.levelUpNeed[levelUp]!!) {
            if (levelUp >= GradeConfig.max) {
                this.level = GradeConfig.max
                this.exp = 0
                break
            }
            levelUp++
            addExp -= GradeConfig.levelUpNeed[levelUp]!!
        }
        if (levelUp >= GradeConfig.max) {
            if (player.isOnline) {
                player.sendMessage(TITLE + if (!this.isVip) "§a成功增加" + now + "点经验!" else "§a你是本服尊贵的§cVIP§a成功增加" + now + "点经验!")
                this.reportLevel(player)
            }
            this.level = GradeConfig.max
            this.exp = 0
        }
        this.level = levelUp
        this.exp = addExp
        if (player.isOnline) {
            player.sendMessage(TITLE + if (!this.isVip) "§a成功增加" + now + "点经验!" else "§a你是本服尊贵的§cVIP§a成功增加" + now + "点经验!")
            this.reportLevel(player)
        }
    }

    fun reportLevel(player: NukkitPlayer?) {
        if (player != null && player.isOnline) {
            if (level >= GradeConfig.max) {
                player.sendMessage("$TITLE§b你已经满级!")
            }
            player.sendMessage(TITLE + if (level < GradeConfig.max) "§b现在等级Lv." + level + " " + exp + "/" + GradeConfig.levelUpNeed[level] else "§b现在等级Lv.$level 0/0")
        }
    }
}