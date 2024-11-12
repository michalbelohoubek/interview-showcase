plugins {
    alias(libs.plugins.interviewshowcase.android.library)
    alias(libs.plugins.interviewshowcase.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.google.play.services.auth)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.functions)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.identity.googleid)
}
android {
    namespace = "cz.michalbelohoubek.interviewshowcase.database"
}
