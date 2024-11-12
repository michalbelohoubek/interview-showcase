plugins {
    `kotlin-dsl`
}

group = "cz.michalbelohoubek.interviewshowcase.app.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "interviewshowcase.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "interviewshowcase.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "interviewshowcase.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "interviewshowcase.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidFeature") {
            id = "interviewshowcase.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
        register("androidFirebase") {
            id = "interviewshowcase.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidHilt") {
            id = "interviewshowcase.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
