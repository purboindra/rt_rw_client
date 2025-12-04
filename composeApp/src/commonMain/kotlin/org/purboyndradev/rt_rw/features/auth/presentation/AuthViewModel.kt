package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.data.remote.params.RequestEmailVerificationParams
import org.purboyndradev.rt_rw.core.data.remote.params.VerifyEmailParams
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.RequestEmailVerificationUseCase
import org.purboyndradev.rt_rw.domain.usecases.SignInUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyEmailUseCase
import org.purboyndradev.rt_rw.domain.usecases.VerifyOtpUseCase
import org.purboyndradev.rt_rw.helper.FieldState
import org.purboyndradev.rt_rw.helper.JWTObject
import org.purboyndradev.rt_rw.helper.Validators

data class OTPUiState(
    val otpLength: Int = 6,
    val isOtpError: Boolean = false,
    val otpValues: List<String> = List(otpLength) { "" },
)

data class VerifyEmailState(
    val success: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val requestEmailVerificationUseCase: RequestEmailVerificationUseCase,
    private val appAuthRepository: AppAuthRepository,
) : ViewModel() {

    private val _loginState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState())
    val loginState = _loginState.asStateFlow()

    private val _requestEmailVerificationState: MutableStateFlow<VerifyEmailState> =
        MutableStateFlow(
            VerifyEmailState()
        )
    val requestEmailVerificationState = _requestEmailVerificationState.asStateFlow()

    private val _verifyOtpState: MutableStateFlow<AuthState> =
        MutableStateFlow(AuthState())
    val verifyOtpState = _verifyOtpState.asStateFlow()

    private val _otpUiState: MutableStateFlow<OTPUiState> =
        MutableStateFlow(OTPUiState())
    val otpUiState = _otpUiState.asStateFlow()

    private val _isLoadingState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val isLoadingState = _isLoadingState.asStateFlow()

    private val _openAlertDialog: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val openAlertDialog = _openAlertDialog.asStateFlow()

    private val _phoneNumberState: MutableStateFlow<FieldState> =
        MutableStateFlow(FieldState())
    val phoneNumberState = _phoneNumberState.asStateFlow()

    private val _emailState: MutableStateFlow<FieldState> = MutableStateFlow(FieldState())
    val emailState = _emailState.asStateFlow()

    private val _openAddEmailDialog = MutableStateFlow(false)
    val openAddEmailDialog = _openAddEmailDialog.asStateFlow()

    val accessToken: Flow<String?> = appAuthRepository.accessTokenFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val hasAuthenticatedBefore: StateFlow<Boolean> = appAuthRepository.isAuthenticated.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    val emailFlow: StateFlow<String?> = appAuthRepository.emailFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun onOpenAddEmailDialogChange(open: Boolean) {
        _openAddEmailDialog.update {
            open
        }
    }

    fun onOpenAlertDialogChange(open: Boolean) {
        _openAlertDialog.update {
            open
        }
    }

    fun updateEmailValue(value: String) {
        _emailState.update {
            it.copy(
                value = value,
                error = if (it.touched) Validators.email(value) else null
            )
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

    fun validatePhoneNumber(value: String): String? =
        Validators.required(
            value,
            "Phone Number"
        ) ?: Validators.minLength(value, 11, "Phone Number")

    fun onUpdatePhoneNumber(phoneNumber: String) {
        _phoneNumberState.update {
            it.copy(
                value = phoneNumber,
                error = if (it.touched) Validators.required(
                    phoneNumber,
                    "Phone Number"
                ) ?: Validators.minLength(
                    phoneNumber,
                    11,
                    "Phone Number"
                ) else null
            )
        }
    }

    fun requestEmailVerification() {

        if (_emailState.value.value.isBlank()) {
            _emailState.update {
                it.copy(
                    error = "Email is required"
                )
            }
            return;
        }

        viewModelScope.launch {
            _requestEmailVerificationState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    success = false
                )
            }

            val params = RequestEmailVerificationParams(
                email = _emailState.value.value
            )

            when (val result = requestEmailVerificationUseCase.invoke(
                params
            )) {
                is Result.Success -> {
                    _requestEmailVerificationState.update {
                        it.copy(
                            success = true
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error.toRes()
                    _requestEmailVerificationState.update {
                        it.copy(
                            error = error
                        )
                    }
                }
            }

            _requestEmailVerificationState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onResetRequestEmailVerificationState() {
        _requestEmailVerificationState.value = VerifyEmailState()
    }

    fun verifyEmail(email: String) {
        viewModelScope.launch {
            _verifyOtpState.update {
                it.copy(
                    isLoading = true
                )
            }

            val otp = _otpUiState.value.otpValues.joinToString("")

            val params = VerifyEmailParams(
                code = otp,
                email = email
            )

            when (val result = verifyEmailUseCase.invoke(
                params
            )) {
                is Result.Success -> {
                    _verifyOtpState.update {
                        it.copy(
                            success = true
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error.toRes()
                    _verifyOtpState.update {
                        it.copy(
                            error = error
                        )
                    }
                }
            }

            _verifyOtpState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

    suspend fun hasAuthenticated(): Boolean {
        val token = accessToken.firstOrNull()

        if (token.isNullOrBlank()) return false

        val payload = JWTObject.decodeJwtPayload(token)

        println("Payload hasAuthenticated: $payload")

        val expSeconds = payload
            ?.get("exp")?.toString()?.toLongOrNull()
            ?: return false

        val nowSeconds = getTimeMillis() / 1000
        return expSeconds > nowSeconds
    }

    fun onResetErrorState() {
        _loginState.value = _loginState.value.copy(
            error = null
        )

        _verifyOtpState.value = _verifyOtpState.value.copy(
            error = null
        )
    }

    fun signIn() {
        _isLoadingState.value = true

        onResetErrorState()

        val phoneNumberError =
            validatePhoneNumber(_phoneNumberState.value.value)

        if (phoneNumberError != null) {
            _phoneNumberState.update {
                it.copy(
                    error = phoneNumberError,
                    touched = true
                )
            }
            _isLoadingState.value = false
            return
        }

        viewModelScope.launch {
            when (val result = signInUseCase(_phoneNumberState.value.value)) {
                is Result.Success -> {

                    val data = result.data

                    val redirectUrl = data.redirectUrl
                    val code = data.code

                    if (code == null) {
                        _loginState.value = _loginState.value.copy(
                            success = true
                        )
                        return@launch
                    }

                    if (code == "USER_NOT_VERIFIED") {
                        redirectUrl?.let {
                            _loginState.update {
                                it.copy(
                                    redirectUrl = redirectUrl,
                                    success = true,
                                    code = code
                                )
                            }
                        }
                        onOpenAlertDialogChange(!_openAlertDialog.value)
                    }
                }

                is Result.Error -> {
                    _loginState.update {
                        it.copy(
                            error = result.error.toRes()
                        )
                    }
                }
            }
            _isLoadingState.value = false
        }
    }

    fun verifyOtp() {
        _verifyOtpState.update {
            it.copy(
                isLoading = true
            )
        }

        val otp = _otpUiState.value.otpValues.joinToString("")

        viewModelScope.launch {
            val result = verifyOtpUseCase(
                phoneNumber = _phoneNumberState.value.value,
                otp = otp,
            )

            when (result) {
                is Result.Success -> {
                    println("Success verify otp: ${result.data}")
                    _verifyOtpState.update {
                        it.copy(
                            success = true,
                        )
                    }

                }

                is Result.Error -> {
                    println("Error verify otp: ${result.error}")
                    val error = result.error.toRes()
                    _verifyOtpState.update {
                        it.copy(
                            error = error,
                        )
                    }
                }
            }
            _verifyOtpState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}
