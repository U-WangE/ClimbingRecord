import com.uwange.build.logic.configurations.configureHiltAndroid
import com.uwange.build.logic.extensions.libs

plugins {
    id("climbingrecord.android.library")
    id("climbingrecord.android.compose")
}

configureHiltAndroid()

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:analytics"))
    implementation(project(":core:common-ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))

    val libs = project.libs

    implementation(libs.findLibrary("kotlinx.serialization.json").get())
    implementation(libs.findLibrary("androidx.compose.navigation").get())
}