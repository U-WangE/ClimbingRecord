import java.util.Properties

plugins {
    id("climbingrecord.android.application")
    id("climbingrecord.android.compose")
}

android {
    namespace = "com.uwange.climbingrecord"

    defaultConfig {
        versionCode = 1
        versionName = "1.0.0"
        applicationId = "com.uwange.climbingrecord"

        val localProperties = Properties()
        localProperties.load(project.rootProject.file("local.properties").bufferedReader())
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = localProperties["KAKAO_NATIVE_APP_KEY"] as String
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${localProperties["KAKAO_NATIVE_APP_KEY"]}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.presentation.main)

    implementation(libs.kakao.user)
}