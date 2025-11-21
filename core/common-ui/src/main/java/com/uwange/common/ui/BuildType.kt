package com.uwange.common.ui

import com.uwange.climbingrecord.common.ui.BuildConfig

private const val RELEASE = "release"

fun isRelease() = BuildConfig.BUILD_TYPE == RELEASE