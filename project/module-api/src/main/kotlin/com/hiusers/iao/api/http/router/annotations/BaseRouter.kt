package com.hiusers.iao.api.http.router.annotations

/**
 * 用于标记路由控制器
 *
 * @param value 控制器路径
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseRouter(
    val value: String = "",
)
