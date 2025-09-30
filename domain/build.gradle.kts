plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.domain"
}

dependencies {
    implementation(project(":core"))
}
