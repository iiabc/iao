package com.hiusers.iao.example

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hiusers.iao.api.http.router.annotations.BaseRouter
import com.hiusers.iao.api.http.router.annotations.NodeRouter
import com.hiusers.iao.api.http.router.annotations.PathParam
import com.hiusers.iao.api.http.router.type.RequestType

@BaseRouter("/salogin")
object TestRouter {

    @NodeRouter("/test/login", RequestType.POST)
    fun testLogin(@PathParam("name") name: String): SaResult {
        // 比对前端提交的账号名称
        if ("zhang" == name) {
            // 根据账号id，进行登录
            StpUtil.login(10001)
            val tokenInfo = StpUtil.getTokenInfo()
            // 返回信息到前端
            return SaResult.data(tokenInfo)
        }
        return SaResult.error("登录失败")
    }

    @NodeRouter("/test/isLogin")
    fun isLogin(): SaResult {
        // 判断是否登录
        if (StpUtil.isLogin()) {
            // 返回token信息
            return SaResult.data(StpUtil.getTokenInfo())
        }
        // 返回登录信息
        return SaResult.error("尚未登录")
    }

}