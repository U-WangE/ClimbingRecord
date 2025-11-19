package com.uwange.build.logic.configurations

import com.uwange.build.logic.extensions.libs
import gradle.kotlin.dsl.accessors._c8e23648a0123cabac06a951e2864907.implementation
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
