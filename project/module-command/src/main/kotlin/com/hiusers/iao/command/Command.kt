package com.hiusers.iao.command

import com.hiusers.iao.util.system.createHelper
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.App

@CommandHeader("iao")
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val stop = subCommand {
        execute<ProxyCommandSender> { _, _, _ ->
            App.shutdown()
        }
    }

}