plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
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
        register("kotlinHilt") {
            id = "climbingrecord.kotlin.hilt"
            implementationClass = "com.uwange.build.logic.configurations.HiltKotlinPlugin"
        }
        register("androidFirebase") {
            id = "climbingrecord.android.firebase"
            implementationClass = "com.uwange.build.logic.configurations.FirebaseAndroidPlugin"
        }
    }
}
