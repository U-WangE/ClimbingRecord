package com.uwange.build.logic.configurations

import com.uwange.build.logic.extensions.libs
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class HiltAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureHiltAndroid()
        }
    }
}

internal fun Project.configureHiltAndroid() {
    with(pluginManager) {
        apply("com.google.devtools.ksp")
        apply("dagger.hilt.android.plugin")
    }

    val libs = project.libs

    dependencies {
        implementation(libs.findLibrary("hilt.android").get())
        implementation(libs.findLibrary("hilt.navigation.compose").get())
        "ksp"(libs.findLibrary("hilt.android.compiler").get())
    }
}
