package cn.hhm.mc.game.bedwars.stage.team

import cn.hhm.mc.game.bedwars.config.MasterConfig
import cn.hhm.mc.game.bedwars.entity.EntityEnderCrystal
import cn.hhm.mc.game.bedwars.stage.BedWarsInstance
import cn.nukkit.entity.data.EntityMetadata
import cn.nukkit.entity.passive.EntityVillager
import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.DoubleTag
import cn.nukkit.nbt.tag.FloatTag
import cn.nukkit.nbt.tag.ListTag
import cn.nukkit.network.protocol.SetEntityDataPacket

class BedWarsTeamInstance(val team: BedWarsTeamData) {
    val players: ArrayList<String> = arrayListOf()
    val gamingPlayers: ArrayList<String> = arrayListOf()
    var copperLevel: Int = 1
    var silverLevel: Int = 1
    var copperNeed = 0
    var silverNeed = 0
    lateinit var villagerShopEntity: EntityVillager
    var isBroken = false
    lateinit var enderCrystal: EntityEnderCrystal

    fun spawnShop(instance: BedWarsInstance) {
        val source = team.shopLocation
        val chunk = source.getLevel().getChunk(source.x.toInt() shr 4, source.z.toInt() shr 4, true)
        val nbt = CompoundTag().putList(ListTag<DoubleTag>("Pos").add(DoubleTag("", source.x)).add(DoubleTag("", source.y)).add(DoubleTag("", source.z)))
                .putList(ListTag<DoubleTag>("Motion").add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)).add(DoubleTag("", 0.0)))
                .putList(ListTag<FloatTag>("Rotation").add(FloatTag("", 0f)).add(FloatTag("", 0f)))
                .putString("bwShop", instance.name)
        val villager = EntityVillager(chunk, nbt)
        villager.setRotation(source.yaw, source.pitch)
        villager.nameTag = MasterConfig.villagerShopDisplayName
        val npk = SetEntityDataPacket()
        val flags = 114688L
        npk.metadata = EntityMetadata().putLong(0, flags).putString(4, MasterConfig.villagerShopDisplayName)
        npk.eid = villager.id
        instance.allPlayers.values.forEach {
            villager.spawnTo(it)
            it.dataPacket(npk)
        }
        this.villagerShopEntity = villager
    }
}