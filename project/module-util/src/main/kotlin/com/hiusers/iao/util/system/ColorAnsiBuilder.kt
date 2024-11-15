package com.hiusers.iao.util.system

/**
 *  来自 Bukkit 颜色风格
 *
 *  用法示例：
 *  - &a：使用预定义的绿色
 *  - &{color}：使用预定义的颜色代码（如：&{RED}）
 *  - &{#RRGGBB}：使用RGB十六进制格式表示的颜色（如：&{#123456}）
 */
object BukkitColorAnsiBuilder {

    private const val MARKER = '&' // 标记符号，用于指示颜色代码
    private const val PREFIX = '{'  // 颜色代码前缀
    private const val SUFFIX = '}'  // 颜色代码后缀

    /**
     * 该函数用于将传入的字符串中的颜色标记转换为相应的ANSI颜色代码。
     *
     * @param string 输入的字符串，可能包含颜色标记
     *
     * @return 转换后的带有ANSI颜色代码的字符串
     */
    fun colored(string: String): String {
        val strBuilder = StringBuilder()  // 用于构建最终的字符串
        val charArray = string.toCharArray()  // 将输入字符串转为字符数组
        var index = 0  // 当前字符索引

        while (index < charArray.size) {
            val c = charArray[index]
            if (c == MARKER) {  // 如果遇到标记符号&，则开始处理颜色
                if (index + 1 < charArray.size) {
                    val code = charArray[index + 1]  // 获取下一个字符作为颜色代码
                    if (code == PREFIX) {  // 如果下一个字符是{，则为自定义颜色代码
                        // 解析自定义颜色代码，截取{和}之间的内容，并进行处理
                        strBuilder.append(AnsiBukkitCode.parse(appendEndSuffix(string.substring(index + 2)).also {
                            index += it.length + 2  // 更新索引，跳过已处理的颜色部分
                        }))
                    } else {  // 否则为预定义颜色代码
                        strBuilder.append(AnsiBukkitCode.parse(code))  // 解析预定义颜色
                        index++  // 跳过颜色代码
                    }
                }
            } else {
                strBuilder.append(c)  // 非颜色标记的字符直接添加到字符串构建器中
            }
            index++  // 移动到下一个字符
        }

        return strBuilder.apply { append("\u001B[0m") }.toString()  // 最后添加重置样式
    }

    /**
     * 该函数用于获取颜色标记代码后的内容，直到遇到}为止。
     *
     * @param string 输入的字符串，应该包含以{开始并以}结束的内容
     *
     * @return 截取到的字符串
     */
    private fun appendEndSuffix(string: String): String {
        val strBuilder = StringBuilder()  // 用于构建解析后的颜色代码部分
        val charArray = string.toCharArray()  // 将输入字符串转为字符数组
        charArray.forEachIndexed { _, c ->
            if (c == SUFFIX) {  // 如果遇到}，结束解析
                return strBuilder.toString()
            } else {
                strBuilder.append(c)  // 添加字符到构建器中
            }
        }
        return strBuilder.toString()  // 返回最终解析出的颜色代码部分
    }

}

interface AnsiBukkitCode {

    val alias: Char?  // 可选的别名，用于简化颜色代码

    /**
     * 将当前颜色代码转换为对应的ANSI转义序列
     * @return 转换后的ANSI转义字符串
     */
    fun transform(): String

    companion object {

        private val customMap: MutableMap<String, AnsiBukkitCode> = mutableMapOf()  // 存储自定义颜色代码的映射

        /**
         * 解析颜色代码，支持十六进制RGB、自定义颜色、预定义颜色和格式
         * @param code 输入的颜色代码字符串
         * @return 对应的ANSI颜色转义序列
         */
        @JvmStatic
        fun parse(code: String): String {
            // 处理十六进制RGB颜色，格式为#RRGGBB
            if (code.startsWith("#") && code.length == 7) {
                val r = Integer.parseInt(code.substring(1, 3), 16)
                val g = Integer.parseInt(code.substring(3, 5), 16)
                val b = Integer.parseInt(code.substring(5, 7), 16)
                return "\u001B[38;2;$r;$g;${b}m"  // 返回ANSI转义序列
            }

            // 处理R,G,B格式的颜色，使用逗号分隔
            if (code.split(Regex("[, ，]")).size == 3) {
                val split = code.split(Regex("[, ，]"))
                return "\u001B[38;2;${split[0].trim()};${split[1].trim()};${split[2].trim()}m"
            }

            // 查找并返回预定义颜色代码
            AnsiBukkitColorCode.entries.find {
                it.name.lowercase() == code.lowercase() || it.alias.toString().lowercase() == code.lowercase()
            }?.also {
                return it.transform()  // 返回预定义颜色的ANSI转义序列
            }

            // 查找并返回格式化样式的ANSI转义序列（如加粗、下划线等）
            AnsiBukkitFormatCode.entries.find {
                it.name.lowercase() == code.lowercase() || it.alias.toString().lowercase() == code.lowercase()
            }?.also {
                return it.transform()  // 返回格式化样式的ANSI转义序列
            }

            // 查找并返回自定义颜色的ANSI转义序列
            customMap[code]?.also { return it.transform() }
            customMap.values.find { it.alias.toString().lowercase() == code.lowercase() }
                ?.also { return it.transform() }

            // 如果未找到任何匹配的颜色代码，直接返回原始的代码
            return code
        }

        /**
         * 根据字符解析颜色代码
         * @param code 单个字符的颜色代码
         * @return 对应的ANSI颜色转义序列
         */
        @JvmStatic
        fun parse(code: Char): String {
            return parse(code.toString())  // 转为字符串并解析
        }

    }
}

/**
 * 预定义的颜色代码枚举类，包含了常用的颜色。
 * 颜色值采用RGB格式，每个颜色包含了红、绿、蓝三种颜色通道的数值。
 */
enum class AnsiBukkitColorCode(val red: Int, val green: Int, val blue: Int, override val alias: Char? = null) :
    AnsiBukkitCode {
    GREEN(85, 255, 85, 'a'),
    RED(255, 85, 85, 'c'),
    BLUE(85, 255, 255, 'b'),
    LightPurple(255, 85, 255, 'd'),
    YELLOW(255, 255, 85, 'e'),
    WHITE(255, 255, 255, 'f'),
    DARKGRAY(85, 85, 85, '8'),
    GRAY(170, 170, 170, '7'),
    GOLD(255, 170, 0, '6'),
    DarkPurple(170, 0, 170, '5'),
    DarkRed(170, 0, 0, '4'),
    DarkAqua(0, 170, 170, '3'),
    DarkGreen(0, 170, 0, '2'),
    Black(0, 0, 0, '0');

    override fun transform(): String {
        // 返回对应颜色的ANSI转义序列
        return "\u001B[38;2;$red;$green;${blue}m"
    }
}

/**
 * 预定义的格式化样式枚举类，包含了常见的文本样式，如加粗、斜体等。
 */
enum class AnsiBukkitFormatCode(val code: String, override val alias: Char?) : AnsiBukkitCode {
    // 重置样式
    RESET("0", 'r'),

    // 加粗
    BOLD("1", 'l'),

    // 斜体
    ITALIC("3", 'o'),

    // 删除线
    STRIKETHROUGH("9", 'm'),

    // 下划线
    UNDERLINE("4", 'n');

    override fun transform(): String {
        // 返回对应格式的ANSI转义序列
        return "\u001B[${code}m"
    }
}

/**
 * 扩展函数：将字符串中的颜色标记转换为控制台的彩色输出
 *
 * @return 转换后的带有颜色的字符串
 */
fun String.colored(): String {
    return BukkitColorAnsiBuilder.colored(this)
}

/**
 * 打印带有颜色的消息到控制台
 *
 * @param message 要打印的消息
 */
fun info(message: Any?) {
    taboolib.common.platform.function.info(message.toString().colored())
}
