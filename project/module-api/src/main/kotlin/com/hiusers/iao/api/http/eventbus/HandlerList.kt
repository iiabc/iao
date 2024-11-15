package com.hiusers.iao.api.http.eventbus

/**
 * 事件处理器列表类，用于管理事件处理器的注册和注销
 */
class HandlerList {

    /**
     * 存储事件处理器的列表
     */
    private val handlerSlots = ArrayList<BaseListener>()

    /**
     * 注册一个事件处理器
     *
     * @param listener 要注册的事件处理器
     */
    fun register(listener: BaseListener) {
        handlerSlots.add(listener)
        // 根据优先级对处理器列表进行排序
        handlerSlots.sortBy { it.priority }
    }

    /**
     * 注销一个事件处理器。
     *
     * @param listener 要注销的事件处理器
     */
    fun unregister(listener: BaseListener) {
        handlerSlots.remove(listener)
    }

    /**
     * 获取已注册的事件处理器列表。
     *
     * @return 已注册的事件处理器列表
     */
    fun getRegisteredListeners(): List<BaseListener> {
        return handlerSlots
    }

}

