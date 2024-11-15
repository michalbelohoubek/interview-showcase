import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.AppVersion
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.configureAndroidCompose
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<BaseAppModuleExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = AppVersion.targetSdk
            }
        }
    }

}