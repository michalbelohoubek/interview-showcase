import cz.michalbelohoubek.interviewshowcase.app.buildlogic.Flavor
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.FlavorDimension
import java.util.Properties

plugins {
    alias(libs.plugins.interviewshowcase.android.application)
    alias(libs.plugins.interviewshowcase.android.application.compose)
    alias(libs.plugins.interviewshowcase.android.application.firebase)
    alias(libs.plugins.interviewshowcase.android.hilt)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    signingConfigs {
        create("release") {
            val signingPropertiesFile = file("signing.properties")
            val signingProperties = Properties()
            signingProperties.load(signingPropertiesFile.inputStream())

            keyAlias = signingProperties.getProperty("KEY_ALIAS")
            keyPassword = signingProperties.getProperty("KEY_PASSWORD")
            storeFile = file(signingProperties.getProperty("STORE_FILE"))
            storePassword = signingProperties.getProperty("STORE_PASSWORD")
        }
    }

    defaultConfig {
        applicationId = "cz.michalbelohoubek.interviewshowcase.app"
        versionCode = 1
        versionName = "1.0.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    flavorDimensions += FlavorDimension.contentType.name
    productFlavors {
        Flavor.values().forEach {
            create(it.name) {
                dimension = it.dimension.name
                if (it.applicationIdSuffix != null) {
                    applicationIdSuffix = it.applicationIdSuffix
                }
            }
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "cz.michalbelohoubek.interviewshowcase"
}

dependencies {
    implementation(project(":feature:sign-in"))
    implementation(project(":feature:competition"))

    implementation(project(":core:ui"))
    implementation(project(":core:design"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.google.play.services.auth)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.mixpanel.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}
