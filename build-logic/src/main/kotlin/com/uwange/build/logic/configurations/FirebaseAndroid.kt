package com.uwange.build.logic.configurations

import com.uwange.build.logic.extensions.libs
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class FirebaseAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureFirebaseAndroid()
        }
    }
}

internal fun Project.configureFirebaseAndroid() {
    dependencies {
        implementation(platform(libs.findLibrary("firebase-bom").get()))
        implementation(libs.findLibrary("firebase-analytics").get())
        implementation(libs.findLibrary("firebase-crashlytics").get())
    }
}
