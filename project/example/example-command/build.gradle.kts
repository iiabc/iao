dependencies {
    compileOnly(project(":project:module-util"))
    compileOnly(project(":project:module-api"))
}

// 子模块
taboolib { subproject = true }