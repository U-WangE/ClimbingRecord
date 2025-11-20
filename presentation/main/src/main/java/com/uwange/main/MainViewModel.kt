package com.uwange.main

import androidx.lifecycle.ViewModel
import com.uwange.common.suspendRunCatching
import com.uwange.domain.model.ForceUpdate
import com.uwange.domain.repository.ConfigureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configureRepository: ConfigureRepository
): ViewModel() {
    private val _forceUpdate = MutableStateFlow<ForceUpdate?>(null)
    val forceUpdate = _forceUpdate.asStateFlow()

    internal suspend fun initConfig() = coroutineScope {
        val forceUpdateJob = launch { checkMinVersion() }
        val loadTermsJob = launch {}

        forceUpdateJob.join()
        loadTermsJob.join()
    }

    private suspend fun checkMinVersion() {
        suspendRunCatching {
            configureRepository.getUpdateInfo()
        }.onSuccess {
            _forceUpdate.value = it
        }.onFailure {

        }
    }
}