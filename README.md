# RSS Reader（Kotlin + Jetpack Compose）

Android 课程项目：一个本地 RSS 阅读器，外加一个独立运行的 OPML 订阅管理工具。

## 工程结构

```
.
├── app/                     # 主阅读器（独立 APK）
│   └── src/main/java/com/example/rssreader/
│       ├── data/local/         # Room：订阅源 / 文章实体、DAO、Database
│       ├── data/remote/        # Retrofit + SimpleXml 解析 RSS
│       ├── data/repository/    # 统一数据入口，协程调度
│       ├── di/                 # 手写依赖容器（不使用 Hilt）
│       ├── ui/                 # Compose 页面：订阅 / 文章 / 详情 / 收藏
│       └── util/               # 时间解析、HTML 转纯文本
└── companion/               # OPML 伴生工具（独立 APK）
    └── src/main/java/com/example/rssreader/companion/
        ├── data/               # Room：订阅源清单
        ├── opml/               # OPML 解析与生成（支持嵌套分类）
        └── ui/                 # 导入 / 导出 / 批量管理
```

## 功能

**app**

- 添加 RSS 订阅源，自动拉取并入库，重复地址去重
- Retrofit + SimpleXml 解析 `channel` / `item`，兼容非严格 XML
- Room 缓存订阅源与文章，外键级联删除、`(feedId, guid)` 唯一索引防重复
- 文章列表 / 详情、全文收藏、收藏列表、未读标记
- 订阅源列表显示未读数，支持下拉刷新与单源刷新

**companion**

- 通过 SAF（`OpenDocument` / `CreateDocument`）导入、导出标准 OPML
- 递归解析 OPML 分类分组，导出时按分组还原
- 多选批量删除、手动添加订阅源
- 独立 Application / 数据库 / Activity，可单独安装运行

## 技术栈

| 用途 | 方案 |
| --- | --- |
| 构建 | Gradle 8.14.3（KTS）+ Version Catalog |
| UI | Jetpack Compose + Material3 + Navigation Compose |
| 本地存储 | Room + KSP |
| 网络 | Retrofit + converter-simplexml |
| 异步 | Kotlin Coroutines + Flow / StateFlow |
| 依赖注入 | 手写 `AppContainer`（未使用 Hilt） |

- `minSdk 24`，`compileSdk 36`，JVM 17
- `java.time` 依赖 core library desugaring
- SimpleXml 排除 `stax` / `xpp3`，避免与 Android 平台类冲突

## 构建

```bash
./gradlew :app:assembleDebug :companion:assembleDebug
```

产物：

- `app/build/outputs/apk/debug/app-debug.apk`
- `companion/build/outputs/apk/debug/companion-debug.apk`

> `local.properties` 中的 `sdk.dir` 需由本机 Android Studio 生成，不提交到仓库。

## 提交记录

按工程推进顺序拆分为 4 次提交：

1. 初始化 Gradle 双模块工程、Version Catalog 与 Wrapper
2. app 数据层：Room 缓存 + Retrofit/SimpleXml 解析 + Repository
3. app UI 层：Compose 页面、导航与 ViewModel
4. companion 模块：OPML 导入导出与批量管理
