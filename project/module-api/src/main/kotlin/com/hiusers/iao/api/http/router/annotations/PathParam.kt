package com.hiusers.iao.api.http.router.annotations

import com.hiusers.iao.api.http.router.type.ParamType

/**
 * 用于标记路由参数
 *
 * @param value 参数名
 * @param type 参数类型 默认是从路径获取
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class PathParam(
    val value: String = "",
    val type: ParamType = ParamType.QUERY
)
