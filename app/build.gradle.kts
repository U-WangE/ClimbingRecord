plugins {
    id("climbingrecord.android.application")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord"

    defaultConfig {
        versionCode = 1
        versionName = "1.0.0"
        applicationId = "com.uwange.climbingrecord"
    }
}

dependencies {
    implementation(projects.presentation.main)
}