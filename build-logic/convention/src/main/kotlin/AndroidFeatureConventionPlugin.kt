import com.android.build.gradle.LibraryExtension
import cz.michalbelohoubek.interviewshowcase.app.buildlogic.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("kotlinx-serialization")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                }
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:ui"))
                add("implementation", project(":core:design"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:common"))

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.navigation.compose").get())
            }
        }
    }
}