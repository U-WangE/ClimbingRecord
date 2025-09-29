plugins {
    id("uwange.android.library")
    id("uwange.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord.domain"
}

dependencies {
    implementation(project(":core"))
}
