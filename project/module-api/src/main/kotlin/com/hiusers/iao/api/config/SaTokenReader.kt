package com.hiusers.iao.api.config

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

object SaTokenReader {

    @Config("sa-token.yml", autoReload = true)
    lateinit var config: Configuration

}