package com.hiusers.iao.example

import cn.dev33.satoken.SaManager
import com.hiusers.iao.example.impl.StpInterfaceImpl
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object StpInterfaceInit {

    @Awake(LifeCycle.ENABLE)
    fun init() {
        SaManager.setStpInterface(StpInterfaceImpl())
    }

}