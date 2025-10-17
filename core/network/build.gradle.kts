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
