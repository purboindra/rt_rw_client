package org.purboyndradev.rt_rw.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData

sealed class SplashNavigationState {
    data object Idle : SplashNavigationState()
    data object NavigateToLogin : SplashNavigationState()
    data object NavigateToHome : SplashNavigationState()
    data class NavigateToActivity(val activityId: String) :
        SplashNavigationState()
}

class SplashViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val appAuthRepository: AppAuthRepository,
) : ViewModel() {
    
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    
    private val _navigationState =
        MutableStateFlow<SplashNavigationState>(SplashNavigationState.Idle)
    val navigationState = _navigationState.asStateFlow()
    
    fun onUpdateNavigationState(value: SplashNavigationState) {
        _navigationState.value = value
    }
    
    fun refreshToken(startDestination: StartDestinationData? = null) {
        _isLoading.value = true
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
                        appAuthRepository.saveTokens(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken
                        )
//                        checkAndExecutePendingNavigation(startDestination)
                    }
                    
                    is Result.Error -> {
                        onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                        println("Error refreshing token: $result")
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
                    checkAndExecutePendingNavigation(it)
                }
            }
        }
    }
    
    private fun checkAndExecutePendingNavigation(startDestination: StartDestinationData? = null) {
        
        println("checkAndExecutePendingNavigation startDestination: $startDestination")
        
        if (startDestination != null) {
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
}