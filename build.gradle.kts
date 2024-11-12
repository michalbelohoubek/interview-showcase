buildscript {
    repositories {
        google()
        mavenCentral()
        maven{
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.kotlin.serializationPlugin)
        classpath(libs.hilt.gradlePlugin)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.google.services)
    }
}

plugins {
    alias(libs.plugins.interviewshowcase.android.application) apply false
    alias(libs.plugins.interviewshowcase.android.application.compose) apply false
    alias(libs.plugins.interviewshowcase.android.application.firebase) apply false
    alias(libs.plugins.interviewshowcase.android.library) apply false
    alias(libs.plugins.interviewshowcase.android.library.compose) apply false
    alias(libs.plugins.interviewshowcase.android.feature) apply false
    alias(libs.plugins.interviewshowcase.android.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}