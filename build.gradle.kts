// 根构建脚本只负责“声明插件版本 + apply false”，不在根工程应用 Kotlin 插件。
// 这样每个 module 只会应用一次 org.jetbrains.kotlin.android，
// 从而避免 "Cannot add extension with name 'kotlin'" 的重复插件报错。
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
}
