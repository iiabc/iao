package com.hiusers.iao

import taboolib.platform.App

/**
 * 主类，入口
 */
object App {

    @JvmStatic
    fun main(args: Array<String>) {
        App.env().group("com.hiusers.iao").version("6.2.0-beta34")

        App.init()
    }

}