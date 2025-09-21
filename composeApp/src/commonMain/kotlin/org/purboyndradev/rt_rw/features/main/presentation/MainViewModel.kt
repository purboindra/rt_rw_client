package org.purboyndradev.rt_rw.features.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllBannersUseCase
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityState

class MainViewModel(
    private val fetchActivitiesUseCase: FetchActivitiesUseCase,
    private val fetchAllBannersUseCase: FetchAllBannersUseCase,
) : ViewModel(
) {

    private val _loadingState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _activitiesState =
        MutableStateFlow(ActivityState(activities = emptyList()))
    val activitiesState = _activitiesState.asStateFlow()

    private val _bannersState = MutableStateFlow(BannerState())
    val bannersState = _bannersState.asStateFlow()

    init {
        viewModelScope.launch {
            onInit()
        }
    }

    suspend fun fetchAllBanners() {
        _bannersState.value = _bannersState.value.copy(
            loading = true
        )
        val result = fetchAllBannersUseCase.invoke()
        when (result) {
            is Result.Success -> {
                val banners = result.data
                _bannersState.value = _bannersState.value.copy(
                    banners
                )
            }

            is Result.Error -> {
                val error = result.error
                _bannersState.value = _bannersState.value.copy(
                    error = error.toRes()
                )
            }
        }
        _bannersState.value = _bannersState.value.copy(
            loading = false
        )
    }

    suspend fun fetchActivities() {
        _activitiesState.value = _activitiesState.value.copy(
            loading = true
        )

        val result = fetchActivitiesUseCase.invoke()

        when (result) {
            is Result.Success -> {
                val activities = result.data
                _activitiesState.value = _activitiesState.value.copy(
                    activities
                )
            }

            is Result.Error -> {
                val error = result.error
                _activitiesState.value = _activitiesState.value.copy(
                    error = error.toRes()
                )
            }
        }

        _activitiesState.value = _activitiesState.value.copy(
            loading = false
        )
    }

    suspend fun onInit() {
        _loadingState.value = true
        coroutineScope {
            val activitiesJob = async { fetchActivities() }
            val bannersJob = async { fetchAllBanners() }
            activitiesJob.await()
            bannersJob.await()
        }
        _loadingState.value = false
    }
}