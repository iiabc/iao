package com.hiusers.iao.api.http.router.type

/**
 * 参数类型枚举
 */
enum class ParamType {

    /**
     * 上下文
     */
    CONTEXT,

    /**
     * 路径参数
     */
    PATH,

    /**
     * Get类型参数 (默认)
     */
    QUERY,

    /**
     * Post类型参数 - JSON
     */
    BODY,

    /**
     * Upload类型参数 - File
     */
    FILE,

    /**
     * Cookie类型参数
     */
    COOKIE,

}
