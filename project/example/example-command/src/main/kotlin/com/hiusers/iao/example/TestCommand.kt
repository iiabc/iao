package com.hiusers.iao.example

import com.hiusers.iao.util.system.createHelper
import com.hiusers.iao.util.system.info
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.platform.App

@CommandHeader("test", description = "测试命令")
object TestCommand {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody
    val hi = subCommand {
        literal("abc") {
            dynamic("数字") {
                execute<ProxyCommandSender> { sender, context, argument ->
                    info("Hello")
                }
            }
        }
        execute<ProxyCommandSender> { sender, context, argument ->
            info("Hello World!")
        }
    }

    @CommandBody
    val stop = subCommand {
        execute<ProxyCommandSender> { sender, context, argument ->
            info("Goodbye World!")
            App.shutdown()
        }
    }

}