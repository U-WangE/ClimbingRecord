plugins {
    id("climbingrecord.android.feature")
}

android {
    namespace = "com.uwange.climbingrecord.auth"
}

dependencies {
    implementation(projects.core.commonUi)
    implementation(libs.kakao.user)
}
