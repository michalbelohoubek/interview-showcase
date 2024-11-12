plugins {
    alias(libs.plugins.interviewshowcase.android.library)
}

dependencies {
}

android {
    namespace = "cz.michalbelohoubek.interviewshowcase.core.model"

    buildTypes {
        release {
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}