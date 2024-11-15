package com.hiusers.iao.example

import com.hiusers.iao.example.container.RoleContainer
import com.hiusers.iao.example.container.UserContainer
import com.hiusers.iao.example.container.UserPermContainer
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf

object UserManager {

    val Database.users get() = this.sequenceOf(UserContainer)

    val Database.roles get() = this.sequenceOf(RoleContainer)

    val Database.userPerms get() = this.sequenceOf(UserPermContainer)

}