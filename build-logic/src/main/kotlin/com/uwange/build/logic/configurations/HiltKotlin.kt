package com.uwange.build.logic.configurations

import com.uwange.build.logic.extensions.libs
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class HiltKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureHiltKotlin()
        }
    }
}

internal fun Project.configureHiltKotlin() {
    with(pluginManager) {
        apply("com.google.devtools.ksp")
    }

    val libs = project.libs

    dependencies {
        implementation(libs.findLibrary("hilt.core").get())
        "ksp"(libs.findLibrary("hilt.compiler").get())
    }
}
