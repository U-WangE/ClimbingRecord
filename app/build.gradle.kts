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

    signingConfigs {
        val keystoreProperties = Properties()
        keystoreProperties.load(project.rootProject.file("keystore.properties").bufferedReader())

        create("release") {
            storeFile = file(keystoreProperties["STORE_FILE_PATH"] as String)
            storePassword = keystoreProperties["STORE_PASSWORD"] as String
            keyAlias = keystoreProperties["KEY_ALIAS"] as String
            keyPassword = keystoreProperties["STORE_PASSWORD"] as String
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.presentation.main)

    implementation(libs.kakao.user)
}