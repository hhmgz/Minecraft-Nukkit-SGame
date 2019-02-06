package cn.hhm.mc.game.base.stage

/**
 * FoundHi
 *
 * @author WetABQ Copyright (c) 2018.07
 * @version 1.0
 */
enum class StageMode(private val mode: Int) {
    WAITING(0),
    PRE_START(1),
    START(2),
    FINNISH(3),
    SCORE(4),
    STOP(5);

    fun toInt(): Int {
        return mode
    }

    companion object {
        fun toState(s: Int): StageMode {
            for (state in StageMode.values()) {
                if (state.mode == s) {
                    return state
                }
            }
            return STOP
        }
    }
}