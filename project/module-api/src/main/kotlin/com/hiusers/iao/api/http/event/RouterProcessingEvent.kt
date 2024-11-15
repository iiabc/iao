package com.hiusers.iao.api.http.event

import com.hiusers.iao.api.http.eventbus.ProxyEvent
import com.hiusers.iao.api.http.router.context.RouterControllerContext
import com.hiusers.iao.api.http.router.context.RouterNodeContext
import io.javalin.http.Context

data class RouterProcessingEvent(
    val controllerData: RouterControllerContext,
    var routerNodeData: RouterNodeContext,
    var context: Context
) : ProxyEvent() {

    val url: String
        get() = context.url()

}
