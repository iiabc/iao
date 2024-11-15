package com.hiusers.iao.env.satoken.http

import cn.dev33.satoken.SaManager
import cn.dev33.satoken.context.model.SaRequest
import cn.dev33.satoken.exception.SaTokenException
import cn.dev33.satoken.servlet.error.SaServletErrorCode
import cn.dev33.satoken.util.SaFoxUtil
import io.javalin.http.Context
import io.javalin.http.Cookie

/**
 * Sa-Token 请求实现类，用于处理 Javalin 框架中的请求
 */
class SaRequestImpl(val ctx: Context): SaRequest {

    /**
     * 获取底层源对象
     *
     * @return 底层源对象
     */
    override fun getSource(): Any {
        return ctx
    }

    /**
     * 在请求体中获取一个值。
     *
     * @param name 参数名称
     * @return 参数值
     */
    override fun getParam(name: String): String {
        // 优先从表单参数中获取，如果不存在则从查询参数中获取
        return ctx.formParam(name) ?: ctx.queryParam(name) ?: ""
    }

    /**
     * 获取请求体中提交的所有参数名称
     *
     * @return 参数名称列表
     */
    override fun getParamNames(): List<String> {
        // 获取表单参数和查询参数的键，并去重
        val formParamKeys = ctx.formParamMap().keys.toList()
        val queryParamKeys = ctx.queryParamMap().keys.toList()
        return (formParamKeys + queryParamKeys).distinct()
    }

    /**
     * 获取请求体中提交的所有参数
     *
     * @return 参数列表
     */
    override fun getParamMap(): Map<String, String> {
        val formParams = ctx.formParamMap()
        val queryParamMap = ctx.queryParamMap()
        val paramMap = mutableMapOf<String, String>()

        // 遍历表单参数，获取第一个值并添加到 paramMap 中
        formParams.forEach { (key, values) ->
            values.firstOrNull()?.let { paramMap[key] = it }
        }

        // 遍历查询参数，获取第一个值并添加到 paramMap 中
        queryParamMap.forEach { (key, values) ->
            values.firstOrNull()?.let { paramMap[key] = it }
        }

        return paramMap
    }

    /**
     * 在请求头中获取一个值。
     *
     * @param name 参数名称
     *
     * @return 参数值
     */
    override fun getHeader(name: String): String {
        return ctx.header(name) ?: ""
    }

    /**
     * 在 Cookie 作用域中获取一个值
     *
     * @param name Cookie 名称
     * @return Cookie 值
     */
    override fun getCookieValue(name: String): String {
        return getCookieLastValue(name) ?: ""
    }

    /**
     * 在 Cookie 作用域中获取一个值 (第一个此名称的)
     *
     * @param name Cookie 名称
     * @return Cookie 值
     */
    override fun getCookieFirstValue(name: String): String? {
        return getFirstCookieValueByName(name)
    }

    /**
     * 在 Cookie 作用域中获取一个值 (最后一个此名称的)
     *
     * @param name Cookie 名称
     * @return Cookie 值
     */
    override fun getCookieLastValue(name: String): String? {
        return getLastCookieValueByName(name)
    }

    /**
     * 返回当前请求的 path (不包括上下文名称)
     *
     * @return 请求的 path
     */
    override fun getRequestPath(): String {
        // 假设上下文路径为空，直接返回请求路径
        return ctx.path()
    }

    /**
     * 返回当前请求的 URL
     *
     * @return 请求的 URL
     */
    override fun getUrl(): String {
        val currDomain = SaManager.getConfig().currDomain
        if (!SaFoxUtil.isEmpty(currDomain)) {
            return "$currDomain${this.requestPath}"
        }
        return ctx.fullUrl()
    }

    /**
     * 返回当前请求的类型
     *
     * @return 请求的类型
     */
    override fun getMethod(): String {
        return ctx.method().name
    }

    /**
     * 转发请求
     *
     * @param path 转发路径
     *
     * @return 转发结果
     */
    override fun forward(path: String): Any? {
        try {
            val response = ctx.res()
            val req = ctx.req()
            req.getRequestDispatcher(path).forward(req, response)
            return null
        } catch (e: Exception) {
            throw SaTokenException(e).setCode(SaServletErrorCode.CODE_20001)
        }
    }

    /**
     * 获取 Cookie 列表
     *
     * @return Cookie 列表
     */
    private fun getCookies(): List<Cookie> {
        val header = ctx.header("Cookie")
        if (header.isNullOrBlank()) {
            return emptyList()
        }

        return header.split("; ").mapNotNull { entry ->
            val parts = entry.split("=", limit = 2)
            if (parts.size == 2) {
                Cookie(parts[0], parts[1])
            } else {
                null
            }
        }
    }

    /**
     * 获取第一个 Cookie 值
     *
     * @param name Cookie 名称
     *
     * @return Cookie 值
     */
    private fun getFirstCookieValueByName(name: String): String? {
        return getCookies().firstOrNull { it.name == name }?.value
    }

    /**
     * 获取最后一个 Cookie 值
     *
     * @param name Cookie 名称
     *
     * @return Cookie 值
     */
    private fun getLastCookieValueByName(name: String): String? {
        return getCookies().lastOrNull { it.name == name }?.value
    }

}