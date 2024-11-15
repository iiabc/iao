package com.hiusers.iao.server.http

import com.hiusers.iao.api.config.ConfigReader.serverPort
import com.hiusers.iao.api.http.event.HttpRequestInterceptionEvent
import com.hiusers.iao.api.http.event.RouterProcessingEvent
import com.hiusers.iao.api.http.event.WebsocketRequestInterceptionEvent
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.api.server.JavalinBefore
import com.hiusers.iao.util.system.info
import io.javalin.Javalin
import taboolib.common.LifeCycle
import taboolib.common.io.runningClasses
import taboolib.common.platform.Awake
import taboolib.library.reflex.Reflex.Companion.invokeMethod

/**
 * Http 服务
 */
@Awake
object HttpServer {

    /**
     * Javalin 服务器实例
     */
    private val server: Javalin by lazy {
        Javalin.create {
            it.showJavalinBanner = false
            it.requestLogger.http { ctx, executionTimeMs ->
                info("&a${ctx.method()} &7- &8${ctx.path()} &7- &8${ctx.status()} &7- &8${executionTimeMs}ms")
                HttpRequestInterceptionEvent(ctx, executionTimeMs).call()
            }
            it.requestLogger.ws { ws ->
                ws.onMessage { context ->
                    WebsocketRequestInterceptionEvent(context, ws).call()
                }
            }
        }
    }

    /**
     * 初始化 Http 服务器
     */
    @Awake(LifeCycle.ENABLE)
    fun init() {

        runningClasses.forEach {
            it.structure.methods.forEach { method ->
                // 初始化 JavalinBefore
                if (method.isAnnotationPresent(JavalinBefore::class.java)) {
                    // 类是否是Object
                    if (it.isSingleton()) {
                        it.getInstance()?.invokeMethod<Unit>(method.name, server)
                    }else {
                        it.newInstance()?.invokeMethod<Unit>(method.name, server)
                    }
                }
            }
        }

        // 遍历路由控制器
        RouterManager.controllers.forEach { (_, controller) ->
            // 遍历每个控制器的节点
            controller.nodes.forEach { node ->
                // 根据请求类型注册路由
                when (node.type) {
                    RequestType.GET -> {
                        server.get("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.POST -> {
                        server.post("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.PUT -> {
                        server.put("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.PATCH -> {
                        server.patch("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.DELETE -> {
                        server.delete("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.HEAD -> {
                        server.head("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }

                    RequestType.OPTIONS -> {
                        server.options("${controller.baseUrl}${node.path}") { ctx ->
                            val event = RouterProcessingEvent(controller, node, ctx)
                            event.call()
                            if (!event.isCancelled()){
                                event.routerNodeData.invoke(ctx)
                            }
                        }
                    }
                }
                info("&a路由注册 &7- &8${controller.baseUrl}${node.path}")
            }
        }

        // 启动服务器
        server.start(serverPort)
        info("&aHttp 服务已启动 &7- &8${serverPort}")
    }

    @Awake(LifeCycle.DISABLE)
    fun shutdown() {
        info("&c正在关闭 Http 服务器&f...")
        server.stop()
        info("&cHttp 服务器已关闭")
    }

}