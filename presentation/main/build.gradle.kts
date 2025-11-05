plugins {
    id("climbingrecord.android.feature")
}
android {
    namespace = "com.uwange.climbingrecord.main"
}

dependencies {
    implementation(projects.core.common)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
}