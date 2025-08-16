package org.purboyndradev.rt_rw.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
            
            if (granted) {
                refreshToken()
                _isLoading.value = false
                return@launch
            }
            
            val deniedAt: Long? = deniedAtFlow.first()
            
            val shouldPrompt = when {
                deniedAt == null -> true
                else -> notificationRepository.hasAWeekPassedSinceNotificationPermissionDenied()
            }
            
            if (shouldPrompt) {
                onUpdateNavigationState(SplashNavigationState.NavigateToNotificationPermission)
            } else {
                refreshToken()
            }
            
            _isLoading.value = false
        }
    }
    
    suspend fun hasAuthenticated(): Boolean {
        val accessToken = appAuthRepository.accessTokenFlow.firstOrNull()
        val refreshToken = appAuthRepository.refreshTokenFlow.firstOrNull()
        
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            return false
        }
        
        val payload = JWTObject.decodeJwtPayload(accessToken)
        
        println("Payload hasAuthenticated: $payload")
        
        val expSeconds = payload
            ?.get("exp")?.toString()?.toLongOrNull()
            ?: return false
        
        val nowSeconds = getTimeMillis() / 1000
        return expSeconds > nowSeconds
    }
    
    
    fun refreshToken(startDestination: StartDestinationData? = null) {
        viewModelScope.launch {
            try {
                val accessToken =
                    appAuthRepository.accessTokenFlow.firstOrNull()
                val refreshToken =
                    appAuthRepository.refreshTokenFlow.firstOrNull()
                
                println("Access Token: $accessToken, Refresh Token: $refreshToken")
                
                // If no user data, navigate to login
                if (accessToken == null || refreshToken == null) {
                    onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                    return@launch
                }
                
                when (val result = refreshTokenUseCase(
                    refreshToken = refreshToken
                )) {
                    is Result.Success -> {
                        
                        println("Success refreshing token: $result")
                        
                        appAuthRepository.saveTokens(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
                        if (startDestination != null) {
                            checkAndExecutePendingNavigation(startDestination)
                        } else {
                            onUpdateNavigationState(SplashNavigationState.NavigateToHome)
                        }
                    }
                    
                    is Result.Error -> {
                        println("Error refreshing token: $result")
                        onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                    }
                }
            } catch (e: NullPointerException) {
                println("Error refresh splash NullPointerException: $e")
                onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
            } catch (e: Exception) {
                println("Error refresh splash: $e")
                onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
            } finally {
                _isLoading.value = false
                startDestination?.let {
                    println("startDestination: $startDestination")
                    checkAndExecutePendingNavigation(it)
                }
            }
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