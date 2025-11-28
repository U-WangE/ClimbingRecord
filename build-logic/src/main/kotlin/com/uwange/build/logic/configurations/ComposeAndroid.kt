package com.uwange.build.logic.configurations

import com.uwange.build.logic.extensions.androidExtension
import com.uwange.build.logic.extensions.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureComposeAndroid() {
    with(plugins) {
        apply("org.jetbrains.kotlin.plugin.compose")
        apply("org.jetbrains.kotlin.plugin.serialization")
    }

    val libs = project.libs

    androidExtension.apply {
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.15"
        }

        buildFeatures {
            compose = true
        }
    }

    dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findLibrary("androidx.compose.ui").get())
        add("implementation", libs.findLibrary("androidx.compose.material3").get())
        add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
        add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())
    }
}
