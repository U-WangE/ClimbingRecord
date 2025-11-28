package com.uwange.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.uwange.analytics.AnalyticsHelper
import com.uwange.analytics.LocalAnalyticsHelper
import com.uwange.designsystem.foundation.ClimbingRecordTheme
import com.uwange.presentation.ui.ClimbingRecordApp
import com.uwange.presentation.ui.rememberClimbingRecordAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MainViewModel by viewModels()
    private var isInitialized: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { isInitialized }

        lifecycleScope.launch {
            viewModel.initConfig()

            isInitialized = false
        }

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CompositionLocalProvider(LocalAnalyticsHelper provides analyticsHelper) {
                val forceUpdate by viewModel.forceUpdate.collectAsStateWithLifecycle()
                val appState = rememberClimbingRecordAppState()

                ClimbingRecordTheme {
                    ClimbingRecordApp(
                        appState = appState,
                        forceUpdate = forceUpdate,

                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    ClimbingRecordTheme {
    }
}