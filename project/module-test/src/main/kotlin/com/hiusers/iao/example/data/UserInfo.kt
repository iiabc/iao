package com.hiusers.iao.example.data

import org.ktorm.entity.Entity

interface UserInfo: Entity<UserInfo> {

    companion object: Entity.Factory<UserInfo>()

    val id: Int

    var name: String

    var password: String

}