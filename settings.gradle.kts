pluginManagement {
    repositories {
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.android") version "1.9.20"
        id("org.jetbrains.kotlin.plugin.compose") version "1.5.4"
        id("com.android.application") version "8.11.2" apply false
        id("com.android.library") version "8.11.2" apply false
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Readio"
include(":app")
include(":feature:auth")
include(":core:auth")
