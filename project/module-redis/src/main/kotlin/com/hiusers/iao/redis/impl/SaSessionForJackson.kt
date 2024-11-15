package com.hiusers.iao.redis.impl

import cn.dev33.satoken.session.SaSession
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Sa-Token 会话对象，用于 Jackson 序列化和反序列化
 * 忽略 "timeout" 属性
 */
@JsonIgnoreProperties("timeout")
class SaSessionForJackson : SaSession {

    /**
     * 默认构造函数
     */
    constructor() : super()

    /**
     * 使用指定的会话 ID 构造一个会话对象
     *
     * @param id 会话的 ID
     */
    constructor(id: String) : super(id)

    /**
     * 伴生对象，用于存储序列化和反序列化相关的常量
     */
    companion object {
        /**
         * 序列化和反序列化时的版本号
         */
        private const val serialVersionUID = -7600983549653130681L
    }
}
