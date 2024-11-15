package com.hiusers.iao.api.http.router.context

import com.hiusers.iao.api.http.router.type.ParamType
import taboolib.library.reflex.LazyAnnotatedClass

/**
 * 路径参数上下文类
 */
data class PathParamContext(
    /**
     * 参数的唯一标识符
     */
    val paramId: String,
    /**
     * 参数的类型
     */
    val type: ParamType,
    /**
     * 参数的类
     */
    val clazz: LazyAnnotatedClass
)
