package com.hiusers.iao.example.data

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hiusers.iao.api.http.router.annotations.BaseRouter
import com.hiusers.iao.api.http.router.annotations.NodeRouter
import com.hiusers.iao.api.http.router.annotations.PathParam
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.api.manager.SecurityManager.checkPassword
import com.hiusers.iao.api.manager.SecurityManager.hashPassword
import com.hiusers.iao.example.data.UserManager.users
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

@BaseRouter("/hello")
object RouterUser {

    @NodeRouter("/add", RequestType.POST)
    fun addUser(@PathParam("name") name: String, @PathParam("password") password: String): SaResult {
        val user = UserInfo {
            this.name = name
            this.password = password.hashPassword()
        }
        val id = database?.users?.add(user)
        return SaResult.data(id)
    }

    @NodeRouter("/login", RequestType.POST)
    fun login(@PathParam("name") name: String, @PathParam("password") password: String): SaResult {
        val user = database?.users?.find {
            it.name eq name
        }
        return if (user != null && password.checkPassword(user.password)) {
            StpUtil.login(user.id)
            return SaResult.data(StpUtil.getTokenInfo())
        } else {
            SaResult.error("用户名或密码错误")
        }
    }

}