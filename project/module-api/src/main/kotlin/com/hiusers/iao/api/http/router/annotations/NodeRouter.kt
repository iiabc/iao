package com.hiusers.iao.api.http.router.annotations

import com.hiusers.iao.api.http.router.type.RequestType


/**
 * 用于标记路由节点
 *
 * @param value 节点路径
 * @param type 请求类型
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class NodeRouter(
    val value: String = "",
    val type: RequestType = RequestType.GET
)
