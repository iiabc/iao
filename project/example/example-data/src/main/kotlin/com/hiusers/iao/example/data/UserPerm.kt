package com.hiusers.iao.example.data

import org.ktorm.entity.Entity

interface UserPerm: Entity<UserPerm> {

    companion object : Entity.Factory<UserPerm>()

    val id: Long

    val permission: String

    val user: UserInfo

}