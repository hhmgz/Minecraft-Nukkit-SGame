package cn.hhm.mc.game.bedwars.entity

import cn.nukkit.Player
import cn.nukkit.entity.Entity
import cn.nukkit.level.Location
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.DoubleTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.AddEntityPacket
import cn.nukkit.network.protocol.RemoveEntityPacket

class EntityEnderCrystal(pos: Location) : Entity(pos.level.getChunk(pos.x.toInt() shr 4, pos.z.toInt() shr 4, true), CompoundTag().putList(ListTag<DoubleTag>("Pos").add(DoubleTag("", pos.x)).add(DoubleTag("", pos.y)).add(DoubleTag("", pos.z))).putList(ListTag<DoubleTag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0))).putList(ListTag<FloatTag>("Rotation").add(FloatTag("", 0f)).add(FloatTag("", 0f)))) {
    var health = 100
    var eid: Long = -1
        private set

    init {
        if (eid == (-1).toLong()) this.eid = Entity.entityCount++
    }

    override fun getNetworkId(): Int {
        return 71
    }

    override fun spawnTo(player: Player) {
        this.getServer().logger.info("Spawn Ender Crystal (eid:" + eid + ") to" + player.name)
        val pk = AddEntityPacket()
        pk.entityRuntimeId = this.eid
        pk.entityUniqueId = this.eid
        pk.x = this.x.toFloat()
        pk.y = this.y.toFloat()
        pk.z = this.z.toFloat()
        pk.yaw = this.yaw.toFloat()
        pk.pitch = this.pitch.toFloat()
        pk.type = this.networkId
        pk.metadata = this.dataProperties
        player.dataPacket(pk)
    }

    override fun despawnFrom(player: Player) {

    }

    override fun despawnFromAll() {

    }

    fun despawn(player: Player) {
        this.getServer().logger.info("Despawn Ender Crystal (eid:" + eid + ") to" + player.name)
        val pk = RemoveEntityPacket()
        pk.eid = eid
        player.dataPacket(pk)
    }

    fun removeEID() {
        this.eid = -1
    }
}
