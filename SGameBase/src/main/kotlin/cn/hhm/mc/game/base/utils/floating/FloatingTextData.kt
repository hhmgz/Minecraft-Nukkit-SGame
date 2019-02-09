package cn.hhm.mc.game.base.utils.floating

import cn.nukkit.level.Position
import cn.nukkit.network.protocol.AddEntityPacket

data class FloatingTextData(val id: String, val eid: Long, val pos: Position, var title: String, var text: String, var addEntityPacket: AddEntityPacket)