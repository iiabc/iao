package com.hiusers.iao.example

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hiusers.iao.api.http.router.annotations.*
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.api.manager.SecurityManager.checkPassword
import com.hiusers.iao.api.manager.SecurityManager.hashPassword
import com.hiusers.iao.example.UserManager.roles
import com.hiusers.iao.example.UserManager.users
import com.hiusers.iao.example.data.Role
import com.hiusers.iao.example.data.UserInfo
import org.ktorm.dsl.eq
import org.ktorm.entity.add
import org.ktorm.entity.find

@BaseRouter("/hello")
object RouterUser {

    @RequestAuth
    @RequireRole("admin")
    @NodeRouter("/add", RequestType.POST)
    fun addUser(
        @PathParam("name") name: String,
        @PathParam("password") password: String,
        @PathParam("role") role: String): SaResult {
        val user = UserInfo {
            this.name = name
            this.password = password.hashPassword()
        }
        val userMode = database?.users?.add(user)
        val roleData = Role {
            this.name = role
            this.user = user
        }
        val roleMode = database?.roles?.add(roleData)
        return SaResult.data("user: $userMode role: $roleMode")
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

    /**
     * 为用户添加角色
     */
    @RequestAuth
    @NodeRouter("/addRole", RequestType.POST)
    fun addRole(@PathParam("userId") userId: Long, @PathParam("roleName") roleName: String): SaResult {
        val user = database?.users?.find { it.id eq userId }
        val role = database?.roles?.find { it.name eq roleName }
        if (user == null) return SaResult.error("用户不存在")
        // 如果没有该角色就添加
        if (role == null) {
            val roleData = Role {
                this.name = roleName
                this.user = user
            }
            database?.roles?.add(roleData)
            return SaResult.ok("已为用户添加角色")
        }
        return SaResult.error("该用户已拥有该角色")
    }

}