package org.purboyndradev.rt_rw.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.purboyndradev.rt_rw.NotificationService
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.NotificationRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData
import org.purboyndradev.rt_rw.helper.JWTObject

sealed class SplashNavigationState {
    data object Idle : SplashNavigationState()
    data object NavigateToLogin : SplashNavigationState()
    data object NavigateToHome : SplashNavigationState()
    data object NavigateToNotificationPermission : SplashNavigationState()
    data class NavigateToActivity(val activityId: String) :
        SplashNavigationState()
}

class SplashViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val appAuthRepository: AppAuthRepository,
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    
    private val refreshMutex = Mutex()
    
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    
    private val _navigationState =
        MutableStateFlow<SplashNavigationState>(SplashNavigationState.Idle)
    val navigationState = _navigationState.asStateFlow()
    
    val deniedAtFlow = notificationRepository.deniedAtFlow
    
    fun onUpdateNavigationState(value: SplashNavigationState) {
        _navigationState.value = value
    }
    
    init {
        viewModelScope.launch {
            val granted = NotificationService.hasGrantedNotificationPermission()
            
            val refreshToken = refreshToken()
            
            val route = if (!granted) {
                val deniedAt = deniedAtFlow.first()
                val shouldPrompt =
                    deniedAt == null || notificationRepository.hasAWeekPassedSinceNotificationPermissionDenied()
                if (shouldPrompt) SplashNavigationState.NavigateToNotificationPermission
                else if (refreshToken) SplashNavigationState.NavigateToHome
                else SplashNavigationState.NavigateToLogin
            } else {
                if (refreshToken) SplashNavigationState.NavigateToHome
                else SplashNavigationState.NavigateToLogin
            }
            
            _isLoading.value = false
            onUpdateNavigationState(route)
        }
    }
    
    fun hasAuthenticated(accessToken: String): Boolean {
        return accessToken.isNotBlank()
    }
    
    fun hasTokenValid(accessToken: String): Boolean {
        val payload = JWTObject.decodeJwtPayload(accessToken)
        
        println("Payload hasTokenValid: $payload")
        
        val expSeconds =
            payload?.get("exp")?.toString()?.toLongOrNull() ?: return false
        
        println("Exp token: $expSeconds")
        
        val nowSeconds = getTimeMillis() / 1000
        
        println("Now token: $nowSeconds, Diff: ${expSeconds - nowSeconds}, Still valid: ${expSeconds > nowSeconds}")
        
        return expSeconds > nowSeconds
    }
    
    private suspend fun refreshToken(): Boolean = refreshMutex.withLock {
        
        val refreshToken =
            appAuthRepository.refreshTokenFlow.firstOrNull().orEmpty()
        val accessToken =
            appAuthRepository.accessTokenFlow.firstOrNull().orEmpty()
        
        val hasAuthenticated = hasAuthenticated(accessToken)
        val hasTokenValid = hasTokenValid(accessToken)
        
        /// MEAN USER NOT LOGGED IN YET
        if (!hasAuthenticated) {
            return false
        }
        
        if (hasTokenValid) {
            return true
        }
        
        when (val res = refreshTokenUseCase(refreshToken)) {
            is Result.Success -> {
                appAuthRepository.saveTokens(
                    res.data.accessToken, res.data.refreshToken
                )
                true
            }
            is Result.Error -> false
        }
    }
    
    private fun checkAndExecutePendingNavigation(startDestination: StartDestinationData) {
        
        println("checkAndExecutePendingNavigation startDestination: $startDestination")
        
        val splashNavigationState = when (startDestination.route) {
            "activity" -> {
                val itemId = startDestination.data as String
                SplashNavigationState.NavigateToActivity(itemId)
            }
            
            else -> {
                SplashNavigationState.NavigateToHome
            }
        }
        onUpdateNavigationState(splashNavigationState)
    }
}