# TabooLib APP SDK (multi-module)

多模块 TabooLib APP 项目模板

## 准备工作

项目结构如下所示

    App
    ├── plugin                     -- 插件打包模块，用于将子模块合并打包
    │   └── build.gradle.kts
    ├── project                    -- 项目目录
    │   ├── core                   -- 核心模块
    │   │   └── build.gradle.kts
    │   └── runtime-app         -- 平台启动类
    │       └── build.gradle.kts
    ├── build.gradle.kts           -- 全局构建文件
    ├── gradle.properties          -- 全局配置
    ├── settings.gradle.kts        -- 全局配置
    ...

### 设置项目名称和版本

在 `settings.gradle.kts` 文件中设置项目名称和版本

### 修改包名

在 `gradle.properties` 文件中修改包名

项目内也需要修改包名

### 项目主入口

在 `build.gradle.kts` 文件中

```kts
attributes("Main-Class" to "io.github.username.project.AppPlugin")
```

## 构建发行版本

发行版本用于正常使用, 不含 TabooLib 本体。

```
./gradlew build
```

## 构建开发版本

开发版本包含 TabooLib 本体, 用于开发者使用, 但不可运行。

```
./gradlew taboolibBuildApi -PDeleteCode
```

> 参数 -PDeleteCode 表示移除所有逻辑代码以减少体积。
