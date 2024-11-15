package com.hiusers.iao.env.satoken.http

import cn.dev33.satoken.context.SaTokenContext
import cn.dev33.satoken.context.model.SaRequest
import cn.dev33.satoken.context.model.SaResponse
import cn.dev33.satoken.context.model.SaStorage
import io.javalin.http.Context
import java.util.regex.Pattern

/**
 * Sa-Token 上下文实现类，用于处理 Javalin 框架中的请求上下文
 */
class SaTokenContextImpl(val context: Context): SaTokenContext {

    /**
     * 获取请求对象
     *
     * @return 请求对象
     */
    override fun getRequest(): SaRequest {
        return SaRequestImpl(context)
    }

    /**
     * 获取响应对象
     *
     * @return 响应对象
     */
    override fun getResponse(): SaResponse {
        return SaResponseImpl(context)
    }

    /**
     * 获取存储对象
     *
     * @return 存储对象
     */
    override fun getStorage(): SaStorage {
        return SaStorageImpl(context)
    }

    /**
     * 校验指定路由匹配符是否可以匹配成功指定路径
     *
     * @param pattern 路由匹配符
     * @param path 路径
     *
     * @return 是否匹配成功
     */
    override fun matchPath(pattern: String, path: String): Boolean {
        val p = Pattern.compile(pattern)
        val m = p.matcher(path)
        return m.matches()
    }

}