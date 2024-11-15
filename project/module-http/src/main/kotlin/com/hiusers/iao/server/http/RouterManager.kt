package com.hiusers.iao.server.http

import com.hiusers.iao.api.http.router.annotations.BaseRouter
import com.hiusers.iao.api.http.router.annotations.NodeRouter
import com.hiusers.iao.api.http.router.context.RouterControllerContext
import com.hiusers.iao.api.http.router.context.RouterNodeContext
import com.hiusers.iao.api.http.router.type.RequestType
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass

/**
 * 用于注册路由组件
 */
@Awake
object RouterManager : ClassVisitor(1) {

    val controllers = HashMap<String, RouterControllerContext>()

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.LOAD
    }

    override fun visitStart(clazz: ReflexClass) {
        val annotationPresent = clazz.structure.isAnnotationPresent(BaseRouter::class.java)
        if (annotationPresent) {
            val annotation = clazz.structure.getAnnotation(BaseRouter::class.java)
            val baseUrl = annotation.enum<String>("value")
            clazz.name?.let {
                controllers[it] = RouterControllerContext(baseUrl)
            }
        }
    }

    override fun visit(method: ClassMethod, owner: ReflexClass) {
        val controller = controllers[owner.name] ?: return
        if (method.isAnnotationPresent(NodeRouter::class.java)) {
            val annotation = method.getAnnotation(NodeRouter::class.java)
            val path = annotation.enum<String>("value")
            val type = annotation.enum("type", RequestType.GET)
            controller.nodes += RouterNodeContext(path, type, method, owner)
        }
    }

}