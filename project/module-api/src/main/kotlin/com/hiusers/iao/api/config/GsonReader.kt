package com.hiusers.iao.api.config

import com.google.gson.Gson

object GsonReader {

    // 延迟初始化的 Gson 对象
    private val gson by lazy {
        Gson()
    }

    /**
     * 获取 Gson 对象。
     *
     * @return Gson 对象。
     */
    fun gsonMapper(): Gson {
        return gson
    }

}