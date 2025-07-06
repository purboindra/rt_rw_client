package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import org.purboyndradev.rt_rw.core.data.datastore.UserRepository
import org.purboyndradev.rt_rw.domain.model.UserDBModel
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.core.domain.model.AuthTokenInfo
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.helper.JWTObject

data class OTPUiState(
    val otpLength: Int = 6,
    val isOtpError: Boolean = false,
    val otpValues: List<String> = List(otpLength) { "" },
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {
    
    private val _signInState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState())
    val signInState = _signInState.asStateFlow()
    
    private val _verifyOtpState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState())
    val verifyOtpState = _verifyOtpState.asStateFlow()
    
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
        _isLoadingState.value = true
        viewModelScope.launch {
            val result = signInUseCase(_phoneNumberState.value)
            when (result) {
                is Result.Success -> {
                    
                    val data = result.data
                    
                    val redirectUrl = data.redirectUrl
                    val code = data.code
                    
                    if (code == "USER_NOT_VERIFIED") {
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
                }
                
                is Result.Error -> {
                    println("Error: $result")
                    _signInState.update {
                        it.copy(
                            error = ""
                        )
                    }
                }
            }
            _isLoadingState.value = false
        }
    }
    
    fun verifyOtp() {
        _isLoadingState.update {
            true
        }
        
        val otp = _otpUiState.value.otpValues.joinToString("")
        
        viewModelScope.launch {
            val result = verifyOtpUseCase(
                phoneNumber = _phoneNumberState.value,
                otp = otp,
            )
            
            when (result) {
                is Result.Success -> {
                    println("Success verify otp: ${result.data}")
                }
                
                is Result.Error -> {
                    println("Error verify otp: ${result.error}")
                }
            }
            _isLoadingState.value = false
        }
    }
}