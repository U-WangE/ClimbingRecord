plugins {
    id("climbingrecord.android.library")
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.uwange.climbingrecord.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.ui)
    implementation(libs.kotlinx.serialization.json)
}