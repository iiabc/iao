package com.hiusers.iao.api.http.eventbus

import taboolib.common.platform.event.ProxyListener
import taboolib.library.reflex.ClassMethod

/**
 * 基础事件监听器类，实现了 `ProxyListener` 接口
 */
data class BaseListener(
    /**
     * 事件监听器的实例
     */
    val instance: Any,
    /**
     * 事件处理方法
     */
    val method: ClassMethod,
    /**
     * 事件处理器的优先级
     */
    val priority: Int,
    /**
     * 是否忽略事件取消标志
     */
    val ignoreCancelled: Boolean
) : ProxyListener {

    /**
     * 调用事件处理方法
     *
     * @param event 要调用的事件
     */
    fun callEvent(event: ProxyEvent) {
        // 如果事件未被取消，则调用事件处理方法
        if (!event.isCancelled()) {
            method.invoke(instance, event)
        }
    }

}

