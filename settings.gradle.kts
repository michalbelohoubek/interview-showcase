pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven{ url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{
            url = uri("https://jitpack.io")
        }
    }
}
rootProject.name = "interview-showcase"
include(":app")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:network")
include(":core:ui")
include(":core:design")
include(":feature:shared-auth")
include(":feature:competition")
include(":feature:sign-in")