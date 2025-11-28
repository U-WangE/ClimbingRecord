plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.hilt")
}

android {
    namespace = "com.uwange.climbingrecord.data"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.network)
}