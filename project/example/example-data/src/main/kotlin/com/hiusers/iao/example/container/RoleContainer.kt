package com.hiusers.iao.example.container

import com.hiusers.iao.api.database.annotations.CreateTable
import com.hiusers.iao.example.data.Role
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.varchar

@CreateTable
object RoleContainer: Table<Role>("roles") {

    val id = long("id").primaryKey().bindTo { it.id }

    val name = varchar("name").bindTo { it.name }

    val userId = long("user_id").references(UserContainer) { it.user }

}
