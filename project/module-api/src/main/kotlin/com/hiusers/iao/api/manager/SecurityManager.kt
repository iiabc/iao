package com.hiusers.iao.api.manager

import cn.dev33.satoken.secure.BCrypt

object SecurityManager {

    /**
     * 加密
     *
     * @return 加密后的哈希值
     */
    fun String.hashPassword(): String {
        // 生成哈希值，salt 是自动生成的
        return BCrypt.hashpw(this, BCrypt.gensalt())
    }

    /**
     * 验证用户输入的密码是否正确
     *
     * @receiver 输入的密码
     * @param storedHash 存储的哈希值
     *
     * @return true 表示密码正确，false 表示密码不正确
     */
    fun String.checkPassword(storedHash: String): Boolean {
        // 使用 BCrypt 提供的验证方法，返回 true 表示密码匹配
        return BCrypt.checkpw(this, storedHash)
    }

}