package com.hiusers.iao.example.container

import com.hiusers.iao.api.database.annotations.CreateTable
import com.hiusers.iao.example.data.UserInfo
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

@CreateTable
object UserContainer: Table<UserInfo>("user_info") {

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val password = varchar("password").bindTo { it.password }

}