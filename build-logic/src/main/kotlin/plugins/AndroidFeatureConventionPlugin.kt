import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("climbingrecord.android.library")
                apply("climbingrecord.android.compose")
            }

            configureHiltAndroid()

            val libs = extensions.libs
            dependencies {
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":domain"))
                add("implementation", project(":core:navigation"))

                add("implementation", libs.findLibrary("androidx.compose.navigation").get())
            }
        }
    }
}
