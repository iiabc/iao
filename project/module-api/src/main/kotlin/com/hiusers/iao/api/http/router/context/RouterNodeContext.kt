package com.hiusers.iao.api.http.router.context

import io.javalin.http.Context
import io.javalin.http.UploadedFile
import io.javalin.http.util.CookieStore
import taboolib.common.inject.ClassVisitor
import taboolib.library.reflex.ClassMethod
import taboolib.library.reflex.ReflexClass
import com.hiusers.iao.api.http.router.annotations.PathParam
import com.hiusers.iao.api.http.router.type.ParamType
import com.hiusers.iao.api.http.router.type.RequestType
import com.hiusers.iao.api.config.GsonReader.gsonMapper
import com.hiusers.iao.util.system.info
import io.javalin.http.HttpStatus

/**
 * 路由节点上下文类。
 */
data class RouterNodeContext(
    /**
     * 路由路径。
     */
    val path: String = "",
    /**
     * 请求类型。
     */
    val type: RequestType,
    /**
     * 方法。
     */
    val method: ClassMethod,
    /**
     * 类反射对象。
     */
    val reflexClass: ReflexClass
) {

    /**
     * 存储参数名称与方法名称的映射关系。
     */
    private val params = ArrayList<PathParamContext>()

    /**
     * 初始化方法。
     */
    init {
        // 遍历方法的参数
        method.parameter.forEach {
            // 如果参数是 Context 类型
            if (it.name == Context::class.java.name) {
                // 将参数添加到 params 中
                params += PathParamContext("context", ParamType.CONTEXT, it)
                return@forEach
            }
            // 如果参数带有 @PathParam 注解
            if (it.isAnnotationPresent(PathParam::class.java)) {
                // 获取注解
                val annotation = it.getAnnotation(PathParam::class.java)
                // 获取注解中的值
                val value = annotation.enum("value", "")
                if (value.isEmpty()) {
                    info("&cPathParam 注解的 value 不能为空,位于 &7- &8 ${method.owner.name}.${method.name}(${it.name})")
                    return@forEach
                }
                // 获取注解中的类型
                val type = annotation.enum("type", ParamType.QUERY)
                // 将参数添加到 params 中
                params += PathParamContext(value, type, it)
                return@forEach
            }
            // 如果参数是普通查询参数
            params += PathParamContext(it.name, ParamType.QUERY, it)
        }
    }

    /**
     * 调用方法。
     *
     * @param context Javalin 的上下文对象。
     */
    fun invoke(context: Context) {
        try {
            // 创建一个存储参数值的列表
            val values = mutableListOf<Any?>()
            // 遍历 params 中的参数
            params.forEach { (paramId, type, clazz) ->
                when (type) {
                    // 如果参数类型是 PATH
                    ParamType.PATH -> {
                        // 从上下文中获取路径参数并添加到 values 中
                        values.add(context.pathParam(paramId))
                    }

                    // 如果参数类型是 CONTEXT
                    ParamType.CONTEXT -> {
                        // 将上下文对象添加到 values 中
                        values.add(context)
                    }

                    // 如果参数类型是 QUERY
                    ParamType.QUERY -> {
                        // 判断参数是否为 list 类型
                        val get = clazz.getter.get()
                        if (get != null && get.isAssignableFrom(Collection::class.java)) {
                            // 如果是 list 类型，获取查询参数并添加到 values 中
                            values.add(context.queryParams(paramId))
                            return@forEach
                        }
                        if (get != null) {
                            // 如果参数类型是 String
                            if (get == String::class.java) {
                                values.add(context.queryParam(paramId))
                                return@forEach
                            }
                            // 如果参数类型是 Int
                            if (get == Int::class.java) {
                                values.add(context.queryParam(paramId)?.toIntOrNull())
                                return@forEach
                            }
                            // 如果参数类型是 Long
                            if (get == Long::class.java) {
                                values.add(context.queryParam(paramId)?.toLongOrNull())
                                return@forEach
                            }
                            // 如果参数类型是 Double
                            if (get == Double::class.java) {
                                values.add(context.queryParam(paramId)?.toDoubleOrNull())
                                return@forEach
                            }
                            // 如果参数类型是 Boolean
                            if (get == Boolean::class.java) {
                                values.add(context.queryParam(paramId)?.toBoolean())
                                return@forEach
                            }
                            return@forEach
                        }
                        // 如果参数类型未知，获取查询参数并添加到 values 中
                        values.add(context.queryParam(paramId))
                    }

                    // 如果参数类型是 BODY
                    ParamType.BODY -> {
                        val get = clazz.getter.get()
                        if (get == ByteArray::class.java) {
                            // 如果参数类型是 ByteArray，获取请求体并添加到 values 中
                            values.add(context.bodyAsBytes())
                            return@forEach
                        }
                        if (get == String::class.java) {
                            // 如果参数类型是 String，获取请求体并添加到 values 中
                            values.add(context.body())
                            return@forEach
                        }
                        if (get != null) {
                            // 如果参数类型是其他类型，获取请求体并转换为指定类型，然后添加到 values 中
                            values.add(context.bodyAsClass(get))
                            return@forEach
                        }
                    }

                    // 如果参数类型是 FILE
                    ParamType.FILE -> {
                        val get = clazz.getter.get() ?: return@forEach
                        if (get == UploadedFile::class.java && paramId.isNotEmpty()) {
                            // 如果参数类型是 UploadedFile，获取上传文件并添加到 values 中
                            values.add(context.uploadedFile(paramId))
                            return@forEach
                        }
                        if (get.isAssignableFrom(Collection::class.java) && paramId.isNotEmpty()) {
                            // 如果参数类型是 Collection，获取上传文件列表并添加到 values 中
                            values.add(context.uploadedFiles(paramId))
                            return@forEach
                        }
                        if (get.isAssignableFrom(Collection::class.java) && paramId.isEmpty()) {
                            // 如果参数类型是 Collection，获取所有上传文件并添加到 values 中
                            values.add(context.uploadedFiles())
                            return@forEach
                        }
                        // 如果参数类型是 Map，获取上传文件映射并添加到 values 中
                        if (get.isAssignableFrom(Map::class.java)) {
                            values.add(context.uploadedFileMap())
                            return@forEach
                        }
                        error("不支持文件类型: $get")
                    }

                    // 如果参数类型是 COOKIE
                    ParamType.COOKIE -> {
                        val get = clazz.getter.get() ?: return@forEach
                        if (get == String::class.java) {
                            // 如果参数类型是 String，获取 Cookie 并添加到 values 中
                            values.add(context.cookie(paramId))
                            return@forEach
                        }
                        if (get.isAssignableFrom(Map::class.java)) {
                            // 如果参数类型是 Map，获取 Cookie 映射并添加到 values 中
                            values.add(context.cookieMap())
                            return@forEach
                        }
                        if (get == CookieStore::class.java) {
                            // 如果参数类型是 CookieStore，获取 CookieStore 并添加到 values 中
                            values.add(context.cookieStore())
                            return@forEach
                        }
                    }
                }
            }

            // 获取类实例
            val instance = ClassVisitor.findInstance(reflexClass)
            // 调用方法
            val back = if (instance != null) {
                method.invoke(instance, *values.toTypedArray())
            } else {
                method.invokeStatic(*values.toTypedArray())
            }
            // 如果返回值不为空且不是 Unit 类型
            if (back != null && back !is Unit) {
                // 使用 Gson 对象将返回值转换为 JSON 字符串
                val toJsonString = gsonMapper().toJson(back)

                // 将 JSON 字符串作为结果返回给上下文
                context.result(toJsonString)
            }
        } catch (e: Exception) {
            context.status(HttpStatus.METHOD_NOT_ALLOWED)
        }

    }

}