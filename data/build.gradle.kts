plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.hilt")
    id("climbingrecord.android.firebase")
}

android {
    namespace = "com.uwange.climbingrecord.data"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.core.network)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
}