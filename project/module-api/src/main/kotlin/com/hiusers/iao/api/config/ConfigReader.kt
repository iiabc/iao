package com.hiusers.iao.api.config

import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigFile
import taboolib.module.configuration.ConfigNode

object ConfigReader {

    @Config(autoReload = true, migrate = true)
    lateinit var config: ConfigFile

    @ConfigNode("server.port")
    val serverPort: Int = 6100

    /**
     * Redis
     */
    @ConfigNode("redis.enable")
    val redisEnable: Boolean = false

}