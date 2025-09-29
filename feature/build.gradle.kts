plugins {
    id("uwange.android.library")
    id("uwange.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.feature"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
}
