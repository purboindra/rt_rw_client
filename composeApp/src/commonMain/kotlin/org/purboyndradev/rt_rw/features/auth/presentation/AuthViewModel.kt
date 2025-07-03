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
    
    private val _signInState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState())
    val signInState = _signInState.asStateFlow()
    
    private val _otpUiState: MutableStateFlow<OTPUiState> =
        MutableStateFlow(OTPUiState())
    val otpUiState = _otpUiState.asStateFlow()
    
    private val _phoneNumberState: MutableStateFlow<String> =
        MutableStateFlow("")
    val phoneNumberState = _phoneNumberState.asStateFlow()
    
    private val _isLoadingState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isLoadingState = _isLoadingState.asStateFlow()
    
    private val _openAlertDialog: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val openAlertDialog = _openAlertDialog.asStateFlow()
    
    fun onOpenAlertDialogChange(open: Boolean) {
        _openAlertDialog.update {
            open
        }
    }
    
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
    
    fun onUpdatePhoneNumber(phoneNumber: String) {
        _phoneNumberState.update {
            phoneNumber
        }
    }
    
    fun signIn() {
        _isLoadingState.update {
            true
        }
        viewModelScope.launch {
            val result = signInUseCase.execute(_phoneNumberState.value)
            when (result) {
                is Result.Success -> {
                    println("Success: ${result.data}")
                    
                    
                    val data = result.data
                    val redirectUrl = data.data?.redirectUrl
                    
                    redirectUrl?.let {
                        _signInState.update {
                            it.copy(
                                redirectUrl = redirectUrl,
                                success = true
                            )
                        }
                    }
                    
                    onOpenAlertDialogChange(!_openAlertDialog.value)
                    
                }
                
                is Result.Error -> {
                    println("Error: ${result.error.name}")
                    _signInState.update {
                        it.copy(
                            error = result.error.name
                        )
                    }
                }
            }
        }
        _isLoadingState.update {
            false
        }
    }
    
}