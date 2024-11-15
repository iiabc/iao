package com.hiusers.iao.redis

import cn.dev33.satoken.SaManager
import com.hiusers.iao.api.config.ConfigReader
import com.hiusers.iao.redis.impl.SaTokenDaoRedisJackson
import com.hiusers.iao.util.system.info
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.expansion.LettuceRedis

object RedisLoader {

    private var redisClient: RedisClient? = null

    private val connection: StatefulRedisConnection<String, String>? = redisClient?.connect()

    @Awake(LifeCycle.ENABLE)
    fun init() {
        if (ConfigReader.redisEnable) {
            try {
                redisClient = LettuceRedis.fastCreateSingleClient(ConfigReader.config.getConfigurationSection("redis")!!).client
                redisClient?.connect()?.sync()?.let {
                    SaManager.setSaTokenDao(SaTokenDaoRedisJackson(it))
                }
                info("&aRedis 连接 &7- &8成功")
            } catch (e: Exception) {
                info("&cRedis 连接 &7- &8失败")
            }
        }else {
            info("&6Redis 未启用 &7- &8使用内存读写数据")
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun close() {
        connection?.close()
        redisClient?.shutdown()
    }

}