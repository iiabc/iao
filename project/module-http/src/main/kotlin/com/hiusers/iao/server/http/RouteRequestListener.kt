package com.hiusers.iao.server.http

import cn.dev33.satoken.stp.StpUtil
import com.hiusers.iao.api.http.event.RouterProcessingEvent
import com.hiusers.iao.api.http.router.annotations.RequestAuth
import com.hiusers.iao.api.http.router.annotations.RequirePermission
import com.hiusers.iao.api.http.router.annotations.RequireRole
import com.hiusers.iao.util.system.info
import org.eclipse.jetty.http.HttpStatus
import taboolib.common.platform.event.SubscribeEvent

object RouteRequestListener {

    @SubscribeEvent(level = 0)
    fun onNode(event: RouterProcessingEvent) {

        val method = event.routerNodeData.method

        // 需要登录注解
        val annotation = method.getAnnotationIfPresent(RequestAuth::class.java)
        if (annotation != null) {
            if (!StpUtil.isLogin()) {
                // 需要登录认证
                event.context.status(HttpStatus.UNAUTHORIZED_401)
                event.setCancelled(true)
                return
            }
        }

        // 角色注解
        if (method.isAnnotationPresent(RequireRole::class.java)) {
            val ann = method.getAnnotation(RequireRole::class.java)

            // 获取 values 属性
            val values = ann.property<List<String>>("values")?: emptyList()
            if (!StpUtil.hasRoleOr(*values.toTypedArray())) {
                info("角色不足，缺少角色之一：${values.joinToString(", ")}")
                event.context.status(HttpStatus.FORBIDDEN_403).result("没有足够的角色访问该资源")
                event.setCancelled(true)
                return
            }
        }

        // 权限注解
        if (method.isAnnotationPresent(RequirePermission::class.java)) {
            val ann = method.getAnnotation(RequirePermission::class.java)

            // 获取 values 属性
            val values = ann.property<List<String>>("values")?: emptyList()
            if (!StpUtil.hasPermissionOr(*values.toTypedArray())) {
                info("权限不足，缺少权限之一：${values.joinToString(", ")}")
                event.context.status(HttpStatus.FORBIDDEN_403).result("没有足够的权限访问该资源")
                event.setCancelled(true)
                return
            }
        }

    }

}