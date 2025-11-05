package com.uwange.main

import androidx.lifecycle.ViewModel
import com.uwange.common.suspendRunCatching
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    internal suspend fun initConfig() = coroutineScope {
        val forceUpdateJob = launch { checkMinVersion() }
        val loadTermsJob = launch {}

        forceUpdateJob.join()
        loadTermsJob.join()
    }

    private suspend fun checkMinVersion() {
        suspendRunCatching {

        }.onSuccess {

        }.onFailure {
        }
    }
}