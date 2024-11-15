package com.hiusers.iao.env.satoken.http

import cn.dev33.satoken.context.model.SaStorage
import io.javalin.http.Context

/**
 * Sa-Token 存储实现类，用于处理 Javalin 框架中的请求作用域存储
 */
class SaStorageImpl(private val context: Context) : SaStorage {

    /**
     * 获取底层源对象
     *
     * @return 底层源对象
     */
    override fun getSource(): Any {
        return context
    }

    /**
     * 在 Request 作用域中写入一个值
     *
     * @param key 键
     * @param value 值
     *
     * @return 对象自身
     */
    override fun set(key: String, value: Any): SaStorageImpl {
        context.attribute(key, value)
        return this
    }

    /**
     * 在 Request 作用域中获取一个值
     *
     * @param key 键
     *
     * @return 值
     */
    override fun get(key: String): Any? {
        return context.attribute<Any>(key)
    }

    /**
     * 在 Request 作用域中删除一个值
     *
     * @param key 键
     *
     * @return 对象自身
     */
    override fun delete(key: String): SaStorageImpl {
        context.attribute(key, null)
        return this
    }

}