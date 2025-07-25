package org.purboyndradev.rt_rw.features.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityState

class MainViewModel(
    private val fetchActivitiesUseCase: FetchActivitiesUseCase,
) : ViewModel(
) {
    private val _activitiesState: MutableStateFlow<ActivityState> =
        MutableStateFlow(
            ActivityState(
                activities = emptyList()
            )
        )
    val activitiesState = _activitiesState.onStart {
        fetchActivities()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActivityState(
            activities = emptyList()
        )
    )
    
    fun fetchActivities() {
        viewModelScope.launch {
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
                        error = when (error) {
                            is ActivityError.InvalidResponse -> "Invalid Response"
                            is ActivityError.Server -> "Internal Server Error"
                            else -> "Unknown Error"
                        }
                    )
                }
            }
            _activitiesState.value = _activitiesState.value.copy(
                loading = false
            )
        }
    }
    
}