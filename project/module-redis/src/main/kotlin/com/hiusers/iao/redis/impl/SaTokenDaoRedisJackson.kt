package com.hiusers.iao.redis.impl

import cn.dev33.satoken.dao.SaTokenDao
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.lettuce.core.api.sync.RedisCommands

/**
 * 使用 Jackson 序列化的 Redis 存储实现 SaTokenDao 接口
 * 该类支持对 Java 对象进行多态性序列化，以保留类型信息，并使用 Redis 作为持久化层
 */
class SaTokenDaoRedisJackson(private val syncCommands: RedisCommands<String, String>) : SaTokenDao {
    
    private var objectMapper: ObjectMapper = ObjectMapper().apply {
        // 启用多态性处理，保留类型信息，支持在反序列化时恢复为原始对象类型
        activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance,
            ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        // 注册 Java 时间模块，以支持 LocalDate、LocalTime 等类型的序列化和反序列化
        val javaTimeModule = JavaTimeModule()
        registerModule(javaTimeModule)
    }  // 用于对象序列化和反序列化的 Jackson ObjectMapper

    /**
     * 从 Redis 获取指定键的值
     */
    override fun get(key: String): String? {
        return syncCommands.get(key)
    }

    /**
     * 设置指定键的值，并指定过期时间
     * 如果 timeout <= 0，则设置为永久有效
     */
    override fun set(key: String, value: String, timeout: Long) {
        if (timeout > 0) {
            syncCommands.setex(key, timeout, value)
        } else {
            syncCommands.set(key, value)
        }
    }

    /**
     * 获取指定的会话对象 SaSessionForJacksonCustomized
     * 使用 Jackson 将 JSON 字符串反序列化为 SaSessionForJacksonCustomized 对象
     */
    override fun getSession(sessionId: String?): SaSessionForJackson? {
        val json = syncCommands.get(sessionId)
        return json?.let {
            objectMapper.readValue(it, SaSessionForJackson::class.java)
        }
    }

    /**
     * 更新指定键的值，同时保留其原有的过期时间
     */
    override fun update(key: String, value: String) {
        val ttl = syncCommands.ttl(key)
        if (ttl > 0) {
            set(key, value, ttl)
        }
    }

    /**
     * 删除指定键
     */
    override fun delete(key: String) {
        syncCommands.del(key)
    }

    /**
     * 获取指定键的过期时间，单位为秒
     */
    override fun getTimeout(key: String): Long {
        return syncCommands.ttl(key)
    }

    /**
     * 更新指定键的过期时间
     * 如果 timeout <= 0，则将其设置为永久有效
     */
    override fun updateTimeout(key: String, timeout: Long) {
        if (timeout > 0) {
            syncCommands.expire(key, timeout)
        } else {
            syncCommands.persist(key)  // 设置为永久
        }
    }

    /**
     * 获取指定键的对象值
     * 使用 Jackson 将 JSON 字符串反序列化为 Object 类型
     */
    override fun getObject(key: String): Any? {
        val json = syncCommands.get(key) ?: return null
        return objectMapper.readValue(json, Any::class.java)
    }

    /**
     * 设置指定键的对象值，并指定过期时间
     * 对象将序列化为 JSON 字符串存储
     */
    override fun setObject(key: String, value: Any, timeout: Long) {
        val json = objectMapper.writeValueAsString(value)
        set(key, json, timeout)
    }

    /**
     * 更新指定键的对象值，同时保留原有的过期时间
     */
    override fun updateObject(key: String, value: Any) {
        val ttl = getTimeout(key)
        if (ttl > 0) {
            setObject(key, value, ttl)
        }
    }

    /**
     * 删除指定键的对象
     */
    override fun deleteObject(key: String) {
        delete(key)
    }

    /**
     * 获取指定键的对象的过期时间，单位为秒
     */
    override fun getObjectTimeout(key: String): Long {
        return getTimeout(key)
    }

    /**
     * 更新指定键的对象的过期时间
     */
    override fun updateObjectTimeout(key: String, timeout: Long) {
        updateTimeout(key, timeout)
    }

    /**
     * 根据前缀和关键词搜索符合条件的键，并根据排序规则和分页参数返回结果
     * @param prefix 键的前缀
     * @param keyword 关键词
     * @param start 开始位置
     * @param size 返回结果的最大数量
     * @param sortType 排序方式，true 表示升序，false 表示降序
     * @return 符合条件的键列表
     */
    override fun searchData(prefix: String, keyword: String, start: Int, size: Int, sortType: Boolean): List<String> {
        val keys = syncCommands.keys("$prefix*$keyword*")
        val sortedKeys = if (sortType) keys.sorted() else keys.sortedDescending()
        return sortedKeys.drop(start).take(size)
    }
}
