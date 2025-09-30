plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.data"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
}
