plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.feature"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
}
