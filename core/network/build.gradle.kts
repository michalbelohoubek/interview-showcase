plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.functions)
    implementation(libs.firebase.crashlytics)

    implementation(libs.okhttp.logging)
}
android {
    namespace = "cz.michalbelohoubek.interviewshowcase.network"

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}
