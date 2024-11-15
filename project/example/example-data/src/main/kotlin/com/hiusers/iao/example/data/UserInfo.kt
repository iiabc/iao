package com.hiusers.iao.example.data

import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.api.manager.DatabaseManager.lazyFetch
import com.hiusers.iao.example.UserManager.roles
import com.hiusers.iao.example.UserManager.userPerms
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter

interface UserInfo: Entity<UserInfo> {

    companion object: Entity.Factory<UserInfo>()

    val id: Long

    var name: String

    var password: String

    // 角色列表
    val roles get() = lazyFetch("roles") { database?.roles?.filter { it.userId eq id } }

    // 权限列表
    val permissions get() = lazyFetch("permissions") { database?.userPerms?.filter { it.userId eq id } }

}