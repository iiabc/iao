package com.hiusers.iao.example

import cn.dev33.satoken.stp.StpUtil
import cn.dev33.satoken.util.SaResult
import com.hiusers.iao.api.http.router.annotations.BaseRouter
import com.hiusers.iao.api.http.router.annotations.NodeRouter
import com.hiusers.iao.api.http.router.annotations.PathParam
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.util.system.info

@BaseRouter("/salogin")
object TestRouter {

    @NodeRouter("/test", RequestType.POST)
    fun doLogin(@PathParam("name") name: String): SaResult {
        // 第一步：比对前端提交的账号名称、密码
        if ("zhang" == name/* && "123456" == pwd*/) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(10001)
            val tokenInfo = StpUtil.getTokenInfo()
            info("token ${StpUtil.getTokenName()}  value: ${StpUtil.getTokenValue()}")

            // Step 3: 返回 token 信息到前端
            return SaResult.data(tokenInfo)
        }
        return SaResult.error("登录失败")
    }

    @NodeRouter("/isLogin")
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