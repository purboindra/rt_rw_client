package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.navigation.Login
import org.purboyndradev.rt_rw.features.navigation.Main

@Composable
fun OTPScreen(navHostController: NavHostController, phoneNumber: String) {
    
    val authViewModel = koinViewModel<AuthViewModel>()
    val otpUiState = authViewModel.otpUiState.collectAsStateWithLifecycle()
    val isLoading = authViewModel.isLoadingState.collectAsStateWithLifecycle()
    val verifyOtpState by authViewModel.verifyOtpState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(Unit) {
        authViewModel.onUpdatePhoneNumber(phoneNumber)
    }
    
    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState.success) {
            navHostController.navigate(
                Main
            ) {
                popUpTo(Login) {
                    inclusive = true
                }
            }
        } else if ((verifyOtpState.error ?: "").isNotBlank()) {
            snackbarHostState.showSnackbar(
                "${verifyOtpState.error}"
            )
        }
    }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Masukkan Kode OTP",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OTPField(
                otpLength = otpUiState.value.otpLength,
                otpValues = otpUiState.value.otpValues,
                isError = otpUiState.value.isOtpError,
                onUpdateOtpValuesByIndex = { index, value ->
                    authViewModel.updateOtpValue(index, value)
                },
                onOtpInputComplete = {}
            )
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(
                onClick = {
                    authViewModel.verifyOtp()
                },
                enabled = otpUiState.value.otpValues.all { it.isNotEmpty() } || !isLoading.value
            ) {
                Text(
                    if (isLoading.value) "Loading..." else "Verify OTP",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
    
}