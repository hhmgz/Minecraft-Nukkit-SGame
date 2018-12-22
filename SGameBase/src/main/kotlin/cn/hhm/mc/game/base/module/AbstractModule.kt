package cn.hhm.mc.game.base.module

import java.io.File

/**
 * SGame
 *
 * @author hhm Copyright (c) 2018/12/22/星期六 22:57
 * version 1.0
 */
abstract class AbstractModule(val file: File) {

    open fun onLoad() {

    }

    open fun onDisable() {

    }

    open fun onEnable() {

    }
}