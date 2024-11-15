package com.hiusers.iao.example.data

import com.hiusers.iao.api.database.annotations.CreateTable
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

@CreateTable
object Users: Table<UserInfo>("user_info") {

    val id = int("id").bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val password = varchar("password").bindTo { it.password }

}