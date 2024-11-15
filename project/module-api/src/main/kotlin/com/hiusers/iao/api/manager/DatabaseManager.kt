package com.hiusers.iao.api.manager

import org.ktorm.database.Database
import org.ktorm.entity.Entity

object DatabaseManager {

    var database: Database? = null

    inline fun <reified T> Entity<*>.lazyFetch(name: String, loader: () -> T): T {
        return this[name] as? T ?: loader().also { this[name] = it }
    }

}