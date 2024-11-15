package com.hiusers.iao.env.satoken.http

import cn.dev33.satoken.context.model.SaResponse
import cn.dev33.satoken.exception.SaTokenException
import cn.dev33.satoken.servlet.error.SaServletErrorCode
import io.javalin.http.Context

/**
 * Sa-Token 响应实现类，用于处理 Javalin 框架中的响应
 */
class SaResponseImpl(private val context: Context) : SaResponse {

    /**
     * 获取底层源对象
     *
     * @return 底层源对象
     */
    override fun getSource(): Any {
        return context
    }

    /**
     * 设置响应状态码
     *
     * @param sc 状态码
     *
     * @return 对象自身
     */
    override fun setStatus(sc: Int): SaResponseImpl {
        context.status(sc)
        return this
    }

    /**
     * 在响应头中写入一个值
     *
     * @param name 参数名称
     * @param value 参数值
     *
     * @return 对象自身
     */
    override fun setHeader(name: String, value: String): SaResponseImpl {
        context.header(name, value)
        return this
    }

    /**
     * 在响应头中添加一个值。
     *
     * @param name 参数名称
     * @param value 参数值
     *
     * @return 对象自身
     */
    override fun addHeader(name: String, value: String): SaResponseImpl {
        context.header(name, value)
        return this
    }

    /**
     * 重定向
     *
     * @param url 重定向的 URL
     *
     * @return 对象自身
     */
    override fun redirect(url: String): Any? {
        try {
            context.redirect(url)
        } catch (e: Exception) {
            throw SaTokenException(e).setCode(SaServletErrorCode.CODE_20002)
        }
        return null
    }

}

