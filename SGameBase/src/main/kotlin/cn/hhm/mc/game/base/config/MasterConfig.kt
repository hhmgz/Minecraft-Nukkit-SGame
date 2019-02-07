package cn.hhm.mc.game.base.config

import cn.hhm.mc.game.base.SGameBase
import cn.nukkit.utils.SimpleConfig


/**
 * @author hhm
 * @date 2017/7/22
 * @since SBedWars
 */

class MasterConfig : SimpleConfig(SGameBase.instance) {
    @Path(value = "服主")
    var realOwner = "hhm"

    @Path(value = "MCRMB.sid")
    var MCRMB_sid = 1

    @Path(value = "MCRMB.key")
    var MCRMB_key = "123456"

    @Path(value = "VIP花费")
    var vipSpend = 5
}
