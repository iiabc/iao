package com.hiusers.iao.env.satoken.server

import cn.dev33.satoken.SaManager
import com.hiusers.iao.api.server.JavalinBefore
import com.hiusers.iao.env.satoken.http.SaTokenContextImpl
import io.javalin.Javalin

object BeforeAction {

    @JavalinBefore
    fun before(server: Javalin) {
        server.before {
            val saTokenContext = SaTokenContextImpl(it)
            SaManager.setSaTokenContext(saTokenContext)
        }
    }

}