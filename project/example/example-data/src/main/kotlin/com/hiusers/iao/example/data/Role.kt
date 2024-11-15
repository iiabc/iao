package com.hiusers.iao.example.data

import org.ktorm.entity.Entity

interface Role: Entity<Role> {

    companion object : Entity.Factory<Role>()

    val id: Long

    var name: String

    var user: UserInfo

}
