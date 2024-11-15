package com.hiusers.iao.api.http.event

import com.hiusers.iao.api.http.eventbus.ProxyEvent
import io.javalin.http.Context

class HttpRequestInterceptionEvent(
    val context: Context,
    val executionTimeMs: Float
) : ProxyEvent()