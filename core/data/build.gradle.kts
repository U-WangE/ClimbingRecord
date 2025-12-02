plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.hilt")
}

android {
    namespace = "com.uwange.climbingrecord.data"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.domain)
    implementation(projects.core.network)

    implementation(libs.firebase.auth)
}