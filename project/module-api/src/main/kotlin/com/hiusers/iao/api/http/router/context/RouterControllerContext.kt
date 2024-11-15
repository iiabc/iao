package com.hiusers.iao.api.http.router.context

/**
 * 路由控制器上下文类
 */
data class RouterControllerContext(
    /**
     * 基础 URL
     */
    val baseUrl: String = "/",
    /**
     * 路由节点列表
     */
    val nodes: ArrayList<RouterNodeContext> = arrayListOf()
)
