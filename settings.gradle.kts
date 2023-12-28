pluginManagement{
    repositories{
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    plugins {
        kotlin("multiplatform") version "1.9.21"
        id("org.jetbrains.compose") version "1.5.11"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.4.0")
}

rootProject.name = "dom-view"

include(":core")
include(":jsApp")
include(":testCompose")
