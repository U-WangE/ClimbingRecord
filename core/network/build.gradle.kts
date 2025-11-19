import org.gradle.kotlin.dsl.dependencies

plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.firebase")
}
android {
    namespace = "com.uwange.climbingrecord.network"

    buildTypes {
        release {
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.firebase.config)
}
