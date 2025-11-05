plugins {
    id("climbingrecord.android.library")
}
android {
    namespace = "com.uwange.climbingrecord.network"

    buildTypes {
        release {
            consumerProguardFiles("consumer-rules.pro")
        }
    }
}

dependencies {
    implementation(libs.firebase.config)
    implementation(libs.kotlinx.serialization.json)
}
