package com.hiusers.iao.api.http.event

import com.hiusers.iao.api.http.eventbus.ProxyEvent
import io.javalin.websocket.WsConfig
import io.javalin.websocket.WsMessageContext

class WebsocketRequestInterceptionEvent(
    val context: WsMessageContext,
    val wsConfig: WsConfig
) : ProxyEvent()