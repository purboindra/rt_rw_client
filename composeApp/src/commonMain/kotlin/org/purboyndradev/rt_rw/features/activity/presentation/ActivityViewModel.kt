package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.data.remote.params.JoinActivityParams
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.CreateActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.DeleteActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.EditActivityUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivitiesUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchActivityByIdUseCase
import org.purboyndradev.rt_rw.domain.usecases.JoinActivityUseCase
import org.purboyndradev.rt_rw.helper.JWTObject
import org.purboyndradev.rt_rw.helper.MessageSnackbarType

class ActivityViewModel(
    private val createActivityUseCase: CreateActivityUseCase,
    private val fetchActivitiesUseCase: FetchActivitiesUseCase,
    private val fetchActivityByIdUseCase: FetchActivityByIdUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val editActivityUseCase: EditActivityUseCase,
    private val joinActivityUseCase: JoinActivityUseCase,
    private val appAuthRepository: AppAuthRepository,
) : ViewModel() {
    
    private val _activitiesState: MutableStateFlow<ActivityState> =
        MutableStateFlow(
            ActivityState(
                activities = emptyList()
            )
        )
    val activitiesState = _activitiesState.asStateFlow()
    
    private val _joinActivityState: MutableStateFlow<JoinActivityState> =
        MutableStateFlow(
            JoinActivityState()
        )
    val joinActivityState = _joinActivityState.asStateFlow()
    
    private val _snackbarType: MutableStateFlow<MessageSnackbarType> =
        MutableStateFlow(
            MessageSnackbarType.INFO
        )
    val snackbarType = _snackbarType.asStateFlow()
    
    private val _hasJoinedActivity: MutableStateFlow<Boolean> =
        MutableStateFlow(
            false
        )
    val hasJoinedActivity = _hasJoinedActivity.asStateFlow()
    
    fun onChangeHasJoinedActivity(hasJoined: Boolean) {
        _hasJoinedActivity.value = hasJoined
    }
    
    fun onChangeSnackbarType(type: MessageSnackbarType) {
        _snackbarType.value = type
    }
    
    suspend fun fetchActivityDetail(id: String) {
        
        val accessToken =
            appAuthRepository.accessTokenFlow.firstOrNull().orEmpty()
        val userId = JWTObject.getUserId(accessToken)
        
        _activitiesState.value = _activitiesState.value.copy(
            loading = true
        )
        
        val result = fetchActivityByIdUseCase.invoke(id)
        
        when (result) {
            is Result.Success -> {
                val activity = result.data
                
                val users = activity.users
                
                val hasJoined = users.any {
                    it.id == userId
                }
                
                onChangeHasJoinedActivity(hasJoined)
                
                _activitiesState.value = _activitiesState.value.copy(
                    activity = activity
                )
            }
            
            is Result.Error -> {
                _activitiesState.value = _activitiesState.value.copy(
                    error = result.error.toRes()
                )
            }
        }
        
        _activitiesState.value = _activitiesState.value.copy(
            loading = false
        )
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
                        error = error.toRes()
                    )
                }
            }
            
            _activitiesState.value = _activitiesState.value.copy(
                loading = false
            )
        }
    }
    
    fun joinActivity(id: String) {
        viewModelScope.launch {
            
            _joinActivityState.value = _joinActivityState.value.copy(
                loading = true,
                success = false,
                error = null
            )
            
            val params = JoinActivityParams(
                id
            )
            
            val result = joinActivityUseCase.invoke(params)
            
            when (result) {
                is Result.Success -> {
                    onChangeSnackbarType(MessageSnackbarType.SUCCESS)
                    _joinActivityState.value = _joinActivityState.value.copy(
                        success = true
                    )
                }
                
                is Result.Error -> {
                    val error = result.error
                    onChangeSnackbarType(MessageSnackbarType.ERROR)
                    _joinActivityState.value = _joinActivityState.value.copy(
                        success = false,
                        error = error.toRes()
                    )
                }
            }
            
            _joinActivityState.value = _joinActivityState.value.copy(
                loading = false
            )
        }
    }
    
}