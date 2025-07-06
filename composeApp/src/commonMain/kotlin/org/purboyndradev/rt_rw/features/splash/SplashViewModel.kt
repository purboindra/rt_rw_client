package org.purboyndradev.rt_rw.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase

sealed class SplashNavigationState {
    data object Idle : SplashNavigationState()
    data object NavigateToLogin : SplashNavigationState()
    data object NavigateToHome : SplashNavigationState()
}

class SplashViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _navigationState =
        MutableStateFlow<SplashNavigationState>(SplashNavigationState.Idle)
    val navigationState = _navigationState.asStateFlow()
    
    fun onUpdateNavigationState(value: SplashNavigationState) {
        _navigationState.value = value
    }
    
    fun refreshToken() {
        
        _isLoading.value = true
        
        viewModelScope.launch {
            
            val userData = userRepository.currentUserData.firstOrNull()
            
            if (userData == null) {
                onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                return@launch
            }
            
            println("Current user data: $userData")
            
            when (val result = refreshTokenUseCase(
                accessToken = userData?.accessToken ?: "",
                refreshToken = userData?.refreshToken ?: ""
            )) {
                is Result.Success -> {
                    userRepository.updateUserAccessToken(
                        newAccessToken = result.data.accessToken,
                        newRefreshToken = result.data.refreshToken,
                    )
                    onUpdateNavigationState(SplashNavigationState.NavigateToHome)
                }
                
                is Result.Error -> {
                    userRepository.clearUserData()
                    onUpdateNavigationState(SplashNavigationState.NavigateToLogin)
                    println("Error refresh splash: $result")
                }
            }
            
            _isLoading.value = false
        }
    }
    
}