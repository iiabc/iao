package com.hiusers.iao.example.container

import com.hiusers.iao.api.database.annotations.CreateTable
import com.hiusers.iao.example.data.UserPerm
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

@CreateTable
object UserPermContainer: Table<UserPerm>("user_permissions") {

    val id = long("id").primaryKey().bindTo { it.id }

    val permission = varchar("permission").bindTo { it.permission }

    val userId = long("user_id").references(UserContainer) { it.user }

}