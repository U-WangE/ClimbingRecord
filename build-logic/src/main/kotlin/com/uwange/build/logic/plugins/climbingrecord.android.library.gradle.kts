import com.uwange.build.logic.configurations.configureHiltAndroid
import com.uwange.build.logic.configurations.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
