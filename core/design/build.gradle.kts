plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.library.compose)
    alias(libs.plugins.compose.compiler)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        checkDependencies = true
    }
    namespace = "cz.michalbelohoubek.interviewshowcase.app.core.design"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.compose.runtime)
    debugImplementation(libs.androidx.lifecycle.viewModelCompose)
    debugImplementation(libs.androidx.savedstate.ktx)
}