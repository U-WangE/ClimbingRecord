import com.uwange.build.logic.configurations.configureKotlinAndroid
import com.uwange.build.logic.configurations.configureHiltAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()