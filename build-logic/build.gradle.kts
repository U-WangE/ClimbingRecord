plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "climbingrecord.android.hilt"
            implementationClass = "com.uwange.build.logic.configurations.HiltAndroidPlugin"
        }
        register("androidFirebase") {
            id = "climbingrecord.android.firebase"
            implementationClass = "com.uwange.build.logic.configurations.FirebaseAndroidPlugin"
        }
    }
}
