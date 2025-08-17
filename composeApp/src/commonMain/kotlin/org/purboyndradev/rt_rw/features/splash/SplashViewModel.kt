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
import org.purboyndradev.rt_rw.NotificationService
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.datastore.NotificationRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData
import org.purboyndradev.rt_rw.helper.JWTObject
import kotlin.coroutines.cancellation.CancellationException

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
        
        println("Access Token: $accessToken, Refresh Token: $refreshToken")
        
        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            return false
        }
        
        val payload = JWTObject.decodeJwtPayload(accessToken)
        
        println("Payload hasAuthenticated: $payload")
        
        val expSeconds = payload
            ?.get("exp")?.toString()?.toLongOrNull()
            ?: return false
        
        println("Exp token: $expSeconds")
        
        val nowSeconds = getTimeMillis() / 1000
        
        println("Now token: $nowSeconds, Diff: ${expSeconds - nowSeconds}, Still valid: ${expSeconds > nowSeconds}")
        
        return expSeconds > nowSeconds
    }
    
    
    suspend fun refreshToken(startDestination: StartDestinationData? = null) {
        try {
            if (!hasAuthenticated()) {
                onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                return
            }
            
            val refreshToken =
                appAuthRepository.refreshTokenFlow.firstOrNull()
            
            val result = refreshTokenUseCase(
                refreshToken = refreshToken ?: ""
            )
            
            when (result) {
                is Result.Success -> {
                    
                    println("Success refreshing token: $result")
                    
                    appAuthRepository.saveTokens(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                    
                    _isLoading.value = false
                    
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: NullPointerException) {
            println("Error refresh splash NullPointerException: $e")
            onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
        } catch (e: Exception) {
            println("Error refresh splash: $e")
            onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
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