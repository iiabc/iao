@file:Suppress("PropertyName", "SpellCheckingInspection")

import io.izzel.taboolib.gradle.App
import io.izzel.taboolib.gradle.Basic
import io.izzel.taboolib.gradle.Database
import io.izzel.taboolib.gradle.DatabaseLettuceRedis
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("io.izzel.taboolib") version "2.0.19"
    kotlin("jvm") version "1.9.24"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "io.izzel.taboolib")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // TabooLib 配置
    taboolib {
        env {
            isDebug = false
            install(App)
            install(Basic, DatabaseLettuceRedis)
            // 依赖下载目录
            fileLibs = "libraries"
            // 资源下载目录
            fileAssets = "assets"
        }
        version {
            taboolib = "6.2.0-beta34"
            coroutines = "1.7.3"
            skipKotlinRelocate = true
            skipTabooLibRelocate = true
        }

        classifier = null
    }

    // 全局仓库
    repositories {
        mavenLocal()
        mavenCentral()
    }

    // 全局依赖
    dependencies {
        taboo("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.24") // 添加依赖，要用taboo
        testImplementation(kotlin("test"))
        taboo(kotlin("reflect"))

        taboo("io.javalin:javalin:6.3.0")
        taboo("org.slf4j:slf4j-simple:2.0.16")

        taboo("com.google.code.gson:gson:2.10.1")

        taboo("cn.dev33:sa-token-core:1.39.0")
        taboo("cn.dev33:sa-token-servlet:1.39.0")
        taboo("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.1")

        taboo("io.lettuce:lettuce-core:6.3.2.RELEASE")

        taboo("org.ktorm:ktorm-core:4.1.1")
        taboo("com.zaxxer:HikariCP:4.0.3")
        taboo("mysql:mysql-connector-java:8.0.32")
    }

    // 编译配置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.jar {
        manifest {
            attributes("Main-Class" to "com.hiusers.iao.App") // 主类
        }
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}
