package com.hiusers.iao.database

import com.hiusers.iao.api.config.ConfigReader.config
import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.util.system.info
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import java.io.Closeable

object DatabaseLoader: Closeable {

    private var dataSource: HikariDataSource? = null

    @Awake(LifeCycle.ENABLE)
    fun init() {
        connect()
    }

    private fun connect() {
        val hikariConfig = HikariConfig()
        val host = config.getString("mysql.host", "localhost")
        val port = config.getInt("mysql.port", 3306)
        val data = config.getString("mysql.database", "iao")
        hikariConfig.jdbcUrl = "jdbc:mysql://$host:$port/$data"
        val username = config.getString("mysql.username", "root")
        hikariConfig.username = username
        val password = config.getString("mysql.password", "password")
        hikariConfig.password = password
        dataSource = HikariDataSource(hikariConfig)
        dataSource?.let {
            database = Database.connect(it)
        }

        database?.useConnection {
            info("&aMySQL 连接 &7- &8成功")
        }?: run {
            info("&cMySQL 连接 &7- &8失败")
        }
    }

    override fun close() {
        dataSource?.close()
    }

}
