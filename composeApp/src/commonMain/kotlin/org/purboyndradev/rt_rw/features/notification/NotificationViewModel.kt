package org.purboyndradev.rt_rw.features.notification

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.purboyndradev.rt_rw.core.data.datastore.NotificationRepository

@Stable
enum class NotificationState {
    PermissionGranted,
    PermissionDenied,
    PermissionDefault
}

class NotificationViewModel(private val notificationRepository: NotificationRepository) :
    ViewModel() {
    
    private val _notificationPermissionState =
        MutableStateFlow(NotificationState.PermissionDefault)
    val notificationPermissionState: StateFlow<NotificationState> =
        _notificationPermissionState
    
    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean> = _isPermissionGranted
    
    private val _isChannelCreated = MutableStateFlow(false)
    val isChannelCreated: StateFlow<Boolean> = _isChannelCreated
    
    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted
    
    suspend fun markNotificationPermissionGranted() {
        notificationRepository.markNotificationPermissionDenied()
    }
    
    fun onUpdatePermissionState(permissionState: NotificationState) {
        _notificationPermissionState.value = permissionState
    }
}
