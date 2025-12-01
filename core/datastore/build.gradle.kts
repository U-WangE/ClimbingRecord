plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}
android {
    namespace = "com.uwange.climbingrecord.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.gson)
}