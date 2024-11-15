package com.hiusers.iao.example.impl

import cn.dev33.satoken.stp.StpInterface
import com.hiusers.iao.api.manager.DatabaseManager.database
import com.hiusers.iao.example.UserManager.users
import org.ktorm.dsl.eq
import org.ktorm.entity.find
import org.ktorm.entity.map
import taboolib.common5.Coerce

class StpInterfaceImpl: StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    override fun getPermissionList(loginId: Any, loginType: String): List<String> {
        val user = database?.users?.find { it.id eq Coerce.toLong(loginId) }
        return user?.permissions?.map { it.permission }?: listOf()
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    override fun getRoleList(loginId: Any, loginType: String): List<String> {
        val user = database?.users?.find { it.id eq Coerce.toLong(loginId) }
        return user?.roles?.map { it.name }?: listOf()
    }

}
