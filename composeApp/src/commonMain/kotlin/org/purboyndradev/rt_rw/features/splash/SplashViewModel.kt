package org.purboyndradev.rt_rw.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RefreshTokenUseCase

class SplashViewModel(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun refreshToken() {
        
        _isLoading.value = true
        
        viewModelScope.launch {
            
            println("Coroutine launched. Attempting to collect user data soon.")
            delay(300) // Experiment with values like 100ms, 300ms, 500ms
            println("Delay finished. Collecting currentUserData.")
            
            userRepository.currentUserData.catch { e ->
                println("Error reading user data: ${e.message}")
                emit(null)
            }.collect { userData ->
                println("Current user data: $userData")
                
//                val result = refreshTokenUseCase.execute(
//                    accessToken = userData?.accessToken ?: "",
//                    refreshToken = userData?.refreshToken ?: ""
//                )
//
//                when (result) {
//                    is Result.Success -> {
//                        println("Success refresh splash: $result")
//                        userRepository.updateUserAccessToken(
//                            newAccessToken = result.data.data?.accessToken.toString(),
//                            newRefreshToken = result.data.data?.accessToken.toString(),
//                        )
//                    }
//
//                    is Result.Error -> {
//                        userRepository.clearUserData()
//                        println("Error refresh splash: $result")
//                    }
//                }
                _isLoading.value = false
            }
        }
    }
    
}