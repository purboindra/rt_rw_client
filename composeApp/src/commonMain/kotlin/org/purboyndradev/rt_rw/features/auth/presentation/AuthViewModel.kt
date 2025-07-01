package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase

data class OTPUiState(
    val otpLength: Int = 6,
    val isOtpError: Boolean = false,
    val otpValues: List<String> = List(otpLength) { "" },
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    
    private val _otpUiState: MutableStateFlow<OTPUiState> =
        MutableStateFlow(OTPUiState())
    val otpUiState = _otpUiState.asStateFlow()
    
    private val _whatsAppNumberState: MutableStateFlow<String> =
        MutableStateFlow("")
    val whatsAppNumberState = _whatsAppNumberState.asStateFlow()
    
    fun updateOtpValue(index: Int, value: String) {
        val newOtpValues = _otpUiState.value.otpValues.toMutableList()
        newOtpValues[index] = value
        _otpUiState.update { currentState ->
            currentState.copy(
                otpValues = newOtpValues,
                isOtpError = false,
            )
        }
    }
    
    fun onUpdateWhatsAppNumber(whatsAppNumber: String) {
        _whatsAppNumberState.update {
            whatsAppNumber
        }
    }
    
    fun signIn() {
        viewModelScope.launch {
            val result = signInUseCase.execute(_whatsAppNumberState.value)
            when (result) {
                is Result.Success -> {
                    println("Success: ${result.data}")
                }
                is Result.Error -> {
                    println("Error: ${result.error.name}")
                }
            }
        }
    }
    
}