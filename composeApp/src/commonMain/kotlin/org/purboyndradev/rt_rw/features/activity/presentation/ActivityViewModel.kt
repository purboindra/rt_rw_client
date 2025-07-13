package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.domain.ActivityError
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase

class ActivityViewModel(
    private val createActivityUseCase: CreateActivityUseCase,
    private val fetchActivitiesUseCase: FetchActivitiesUseCase,
    private val fetchActivityByIdUseCase: FetchActivityByIdUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val editActivityUseCase: EditActivityUseCase,
    private val activityId: String,
) : ViewModel() {
    
    private val _activitiesState: MutableStateFlow<ActivityState> =
        MutableStateFlow(
            ActivityState(
                activities = emptyList()
            )
        )
    val activitiesState = _activitiesState.onStart {
        fetchActivityDetail()
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ActivityState(
            activities = emptyList()
        )
    )
    
    fun fetchActivityDetail() {
        viewModelScope.launch {
            _activitiesState.value = _activitiesState.value.copy(
                loading = true
            )
            
            val result = fetchActivityByIdUseCase.invoke(activityId)
            
            when (result) {
                is Result.Success -> {
                    val activity = result.data
                    _activitiesState.value = _activitiesState.value.copy(
                        activities = listOf(activity)
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