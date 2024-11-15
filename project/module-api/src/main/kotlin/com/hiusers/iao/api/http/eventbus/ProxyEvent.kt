package com.hiusers.iao.api.http.eventbus

/**
 * 代理事件类
 */
open class ProxyEvent {

    /**
     * 表示事件是否被取消
     */
    private var isCancelled = false

    /**
     * 允许取消事件的标志
     */
    private var allowCancelled: Boolean = true

    /**
     * 获取事件处理器的列表
     */
    fun getHandlers(): HandlerList {
        return EventManager.getHandlers(this::class.java.name)
    }

    /**
     * 检查事件是否被取消
     */
    fun isCancelled(): Boolean {
        return isCancelled
    }

    /**
     * 设置事件是否被取消
     */
    fun setCancelled(value: Boolean) {
        if (allowCancelled) {
            isCancelled = value
        } else {
            error("该事件不允许取消")
        }
    }

    /**
     * 调用事件
     */
    fun call(): Boolean {
        EventManager.callEvent(this)
        return isCancelled
    }

}
