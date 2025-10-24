import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltAndroid() {
    with(pluginManager) {
        apply("com.google.devtools.ksp")
        apply("dagger.hilt.android.plugin")
    }

    val libs = extensions.libs

    dependencies {
        add("implementation", libs.findLibrary("hilt.android").get())
        add("implementation", libs.findLibrary("hilt.navigation.compose").get())
        add("ksp", libs.findLibrary("hilt.android.compiler").get())
    }
}
