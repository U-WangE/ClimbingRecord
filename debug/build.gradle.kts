plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.debug"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.commonUi)
    implementation(projects.core.analytics)
    implementation(projects.core.navigation)

}
