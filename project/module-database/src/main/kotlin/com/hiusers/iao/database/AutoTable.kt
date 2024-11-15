package com.hiusers.iao.database

import com.hiusers.iao.api.database.annotations.CreateTable
import com.hiusers.iao.api.manager.DatabaseManager
import com.hiusers.iao.util.system.info
import org.ktorm.schema.Column
import taboolib.common.Inject
import taboolib.common.LifeCycle
import taboolib.common.inject.ClassVisitor
import taboolib.common.platform.Awake
import taboolib.library.reflex.ReflexClass
import java.sql.SQLException

@Inject
@Awake
object AutoTable: ClassVisitor(10) {

    override fun getLifeCycle(): LifeCycle {
        return LifeCycle.ENABLE
    }

    override fun visitStart(clazz: ReflexClass) {
        if (clazz.hasAnnotation(CreateTable::class.java)) {
            info("&b>>>>>>>>>>&6${clazz.simpleName}")
            var tableName = ""
            val columnSql = mutableListOf<String>() // 使用列表来存储列的 SQL 语句
            clazz.structure.fields.forEach {
                if (it.fieldType == Column::class.java) {
                    val column = it.get(null) as Column<*>
                    tableName = column.table.tableName
                    val columnName = column.name

                    // 构建字段的 SQL 类型
                    val sqlType = when (val columnType = column.sqlType.typeName) {
                        "int" -> "INT"
                        "varchar" -> "VARCHAR(255)" // 默认为 255 长度
                        "text" -> "TEXT"
                        "boolean" -> "BOOLEAN"
                        "long" -> "BIGINT"
                        "float" -> "FLOAT"
                        "double" -> "DOUBLE"
                        "decimal" -> "DECIMAL(10, 2)" // 默认为 DECIMAL 类型，支持精度和小数位
                        "date" -> "DATE"
                        "timestamp" -> "TIMESTAMP"
                        "blob" -> "BLOB"
                        "binary" -> "BINARY"
                        "char" -> "CHAR(1)" // 默认 CHAR 类型，长度为 1
                        else -> columnType.uppercase() // 默认处理其他类型，直接转换为大写
                    }

                    // 处理 id 字段，添加约束
                    if (columnName == "id") {
                        columnSql.add("$columnName $sqlType NOT NULL AUTO_INCREMENT PRIMARY KEY")
                    } else {
                        // 对于其他字段，添加 NOT NULL 或其他约束（如果需要的话）
                        columnSql.add("$columnName $sqlType")
                    }
                }
            }

            if (tableName.isNotEmpty()) {
                val createTableSql = "CREATE TABLE IF NOT EXISTS $tableName (${columnSql.joinToString(", ")});"
                info("&csql: &6$createTableSql")
                info("&edatabase ${DatabaseManager.database}")
                try {
                    DatabaseManager.database?.useConnection { conn ->
                        info("&b连接成功")
                        val stmt = conn.createStatement()
                        // 执行 SQL 语句
                        stmt.executeUpdate(createTableSql)
                        stmt.close()
                    }
                } catch (e: SQLException) {
                    info("Error executing SQL: ${e.message}")
                }
            }
        }
    }

}