import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser{
            commonWebpackConfig{
                // TODO: 解决重复打开的问题
                devServer = (devServer?: KotlinWebpackConfig.DevServer()).apply {
                    open = false
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting  {
            dependencies {
                implementation(project(":core"))
                implementation(compose.ui)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
            }
        }
    }
}

compose.experimental {
    web.application {}
}