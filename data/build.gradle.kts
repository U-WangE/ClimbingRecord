plugins {
    id("uwange.android.library")
    id("uwange.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.data"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
}
