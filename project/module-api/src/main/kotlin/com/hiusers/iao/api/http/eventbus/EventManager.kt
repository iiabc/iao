package com.hiusers.iao.api.http.eventbus

import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import java.util.concurrent.ConcurrentHashMap

/**
 * 事件管理器
 */
@Awake
object EventManager : ClassVisitor(1) {

    /**
     * 存储事件处理器
     */
    private val handlers = ConcurrentHashMap<String, HandlerList>()

    /**
     * 获取指定事件名称的处理器列表
     *
     * @param event 事件名称
     *
     * @return 对应的处理器列表
     */
    fun getHandlers(event: String): HandlerList {
        return handlers.getOrPut(event) { HandlerList() }
    }

    /**
     * 触发事件
     *
     * @param event 要触发的事件
     */
    fun callEvent(event: ProxyEvent) {
        // 获取事件对应的处理器列表
        val listeners = handlers[event.javaClass.name]
        // 遍历处理器列表中的注册监听器
        listeners?.getRegisteredListeners()?.forEach {
            // 如果忽略取消标志为 true 或者事件未被取消，则调用监听器的事件处理方法
            if (it.ignoreCancelled || !event.isCancelled()) {
                it.callEvent(event)
            }
        }
    }

    /**
     * 获取生命周期
     *
     * @return 生命周期枚举
     */
    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.ENABLE
    }

    /**
     * 访问类方法，用于处理事件订阅
     *
     * @param method 类方法
     * @param owner 类反射对象
     */
    override fun visit(method: ClassMethod, owner: ReflexClass) {
        // 检查方法是否带有 @SubscribeEvent 注解
        if (method.isAnnotationPresent(SubscribeEvent::class.java)) {
            // 获取 @SubscribeEvent 注解
            val annotation = method.getAnnotation(SubscribeEvent::class.java)
            // 获取方法的第一个参数
            method.parameter.firstOrNull()?.let { parameter ->
                try {
                    // 获取参数的实例
                    parameter.instance
                    // 获取处理器列表
                    val handlerList = getHandlers(parameter.name)
                    // 获取类实例
                    val findInstance = findInstance(owner)
                    if (findInstance != null) {
                        // 获取注解中的级别和忽略取消标志
                        val level = annotation.enum("level", -1)
                        val ignoreCancelled = annotation.enum("ignoreCancelled", false)
                        // 注册监听器
                        handlerList.register(BaseListener(findInstance, method, level, ignoreCancelled))
                    }
                } catch (_: Exception) {
                    // 处理异常，忽略错误
                }
            }
        }
    }

}
