package cn.hhm.mc.game.base.utils

enum class BroadcastRange() {
    ALL, PLAYING, WATCH, ALIVE, DEATHWATCH//All是所有人，包括玩家和观战者（不包括死亡观战的），playing可分为alive和deathwatch（死亡观战）
}

fun Array<out BroadcastRange>.isOverlap(): Boolean {
    if (this.contains(BroadcastRange.ALL) && this.size != 1) return true
    if (this.contains(BroadcastRange.PLAYING) && (this.contains(BroadcastRange.ALIVE) || this.contains(BroadcastRange.DEATHWATCH))) {
        return true
    }
    return false
}

fun Array<out BroadcastRange>.isNeedTransmit(): Boolean {
    return this.contains(BroadcastRange.ALIVE) || this.contains(BroadcastRange.DEATHWATCH)
}