package com.hiusers.iao.example.data

import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf

object UserManager {

    val Database.users get() = this.sequenceOf(Users)

}