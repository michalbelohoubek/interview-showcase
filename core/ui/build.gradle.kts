plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

dependencies {
    implementation(project(":core:design"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.datetime)

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.tracing.ktx)
}
android {
    namespace = "cz.michalbelohoubek.interviewshowcase.app.core.ui"
}
