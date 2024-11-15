package com.hiusers.iao.env.satoken

import cn.dev33.satoken.SaManager
import cn.dev33.satoken.config.SaTokenConfig
import com.hiusers.iao.api.config.SaTokenReader.config
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object SaTokenLoader {

    @Awake(LifeCycle.ENABLE)
    fun init() {
        val sc = SaTokenConfig()
        sc.tokenName = config.getString("token-name", "Authorization")
        sc.timeout = config.getLong("timeout", 2592000)
        sc.activeTimeout = config.getLong("active-timeout", -1)
        sc.dynamicActiveTimeout = config.getBoolean("dynamic-active-timeout", false)
        sc.isConcurrent = config.getBoolean("is-concurrent", true)
        sc.isShare = config.getBoolean("is-share", true)
        sc.maxLoginCount = config.getInt("max-login-count", 12)
        sc.maxTryTimes = config.getInt("max-try-times", 12)
        sc.isReadBody = config.getBoolean("is-read-body", true)
        sc.isReadHeader = config.getBoolean("is-read-header", true)
        sc.isReadCookie = config.getBoolean("is-read-cookie", true)
        sc.isWriteHeader = config.getBoolean("is-write-header", false)
        sc.tokenStyle = config.getString("token-style", "uuid")
        sc.dataRefreshPeriod = config.getInt("data-refresh-period", 30)
        sc.tokenSessionCheckLogin = config.getBoolean("token-session-check-login", true)
        sc.autoRenew = config.getBoolean("auto-renew", true)
        sc.tokenPrefix = config.getString("token-prefix", null)
        sc.isPrint = config.getBoolean("is-print", true)
        sc.isLog = config.getBoolean("is-log", false)
        sc.logLevel = config.getString("log-level", "trace")
        sc.logLevelInt = config.getInt("log-level-int", 1)
        sc.isColorLog = config.getBoolean("is-color-log", true)
        sc.jwtSecretKey = config.getString("jwt-secret-key", null)
        sc.sameTokenTimeout = config.getLong("same-token-timeout", 86400)
        sc.currDomain = config.getString("curr-domain", null)
        sc.checkSameToken = config.getBoolean("check-same-token", false)

        SaManager.setConfig(sc)
    }

}