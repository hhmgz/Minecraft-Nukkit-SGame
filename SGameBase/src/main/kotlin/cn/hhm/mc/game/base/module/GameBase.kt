package cn.hhm.mc.game.base.module

import cn.hhm.mc.game.base.stage.GameInstance
import cn.hhm.mc.game.base.stage.GameRoom
import cn.hhm.mc.game.base.utils.Games
import java.io.File

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 23:04
 * version 1.0
 */
abstract class GameBase(val type: Games) : AbstractModule() {
    val gameRooms: HashMap<Int, GameRoom> = hashMapOf()
    var gameRoomClass: Class<out GameRoom> = GameRoom::class.java
    var gameInstanceClass: Class<out GameInstance> = GameInstance::class.java

    open fun loadRooms() {
        var success = 0
        var fail = 0
        File(this.absolutePath.toString() + "/rooms/").list().forEach {
            val r = it.replace(".yml", "").toInt()
            try {
                val room = gameRoomClass.getConstructor(Games::class.java, Int::class.java).newInstance(type, r)
                gameRooms[r] = room
                success++
            } catch (e: Throwable) {
                fail++
            }
        }
        this.info("§3加载房间数据完毕。§2成功 $success 个,§c失败 $fail 个,共${success + fail}个")
    }

    override fun info(msg: String) {
        super.info(type.mainTitle + msg)
    }
}