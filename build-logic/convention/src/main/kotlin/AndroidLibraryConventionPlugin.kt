import com.android.build.gradle.LibraryExtension
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.AppVersion
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.configureFlavors
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AppVersion.targetSdk
                configureFlavors(this)
            }
        }
    }
}
