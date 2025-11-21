plugins {
    id("climbingrecord.android.feature")
}
android {
    namespace = "com.uwange.climbingrecord.presentation"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.debug)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
}