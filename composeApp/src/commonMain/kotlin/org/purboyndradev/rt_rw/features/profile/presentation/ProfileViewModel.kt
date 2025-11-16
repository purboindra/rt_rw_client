package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.datastore.AppAuthRepository

class ProfileViewModel(
    private val appAuthRepository: AppAuthRepository
) : ViewModel() {

    private val _logOutState = MutableStateFlow(LogoutState())
    val logOutState = _logOutState.asStateFlow()

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