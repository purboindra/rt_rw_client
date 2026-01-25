package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.FetchCurrentUserUseCase

class ProfileViewModel(
    private val appAuthRepository: AppAuthRepository,
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase
) : ViewModel() {

    private val _logOutState = MutableStateFlow(LogoutState())
    val logOutState = _logOutState.asStateFlow()

    private val _currentUserState = MutableStateFlow<CurrentUserState>(CurrentUserState())
    val currentUserState = _currentUserState.asStateFlow()

    val userNameFlow: StateFlow<String?> = appAuthRepository.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val userIdFlow: StateFlow<String?> = appAuthRepository.userIdFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val emailFlow: StateFlow<String?> = appAuthRepository.emailFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUserState.update {
                it.copy(
                    loading = true
                )
            }

            val userId = userIdFlow.firstOrNull { it != null }

            when (val response = fetchCurrentUserUseCase(id = userId ?: "")) {
                is Result.Success -> {
                    _currentUserState.update {
                        it.copy(
                            data = response.data
                        )
                    }
                }

                is Result.Error -> {
                    _currentUserState.update {
                        it.copy(
                            error = response.error.toRes()
                        )
                    }
                }
            }

            _currentUserState.update {
                it.copy(
                    loading = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logOutState.update {
                it.copy(
                    loading = true
                )
            }
            appAuthRepository.clearUserPrefs()
            _logOutState.update {
                it.copy(
                    loading = false,
                    success = true
                )
            }
        }
    }
}