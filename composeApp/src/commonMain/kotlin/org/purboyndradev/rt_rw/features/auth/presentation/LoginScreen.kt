package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.TelegramLauncher
import org.purboyndradev.rt_rw.features.components.OpenTelegramDialog
import org.purboyndradev.rt_rw.features.navigation.Main
import org.purboyndradev.rt_rw.features.navigation.OTP

@Composable
fun LoginScreen(navHostController: NavHostController) {

    val authViewModel = koinViewModel<AuthViewModel>()

    val snackbarHostState = remember { SnackbarHostState() }

    val isLoadingState by
    authViewModel.isLoadingState.collectAsStateWithLifecycle()
    val authState by authViewModel.loginState.collectAsStateWithLifecycle()
    val openAlertDialog by
    authViewModel.openAlertDialog.collectAsStateWithLifecycle()
    val phoneNumberState by authViewModel.phoneNumberState.collectAsStateWithLifecycle()
    val hasAuthenticatedBefore by authViewModel.hasAuthenticatedBefore.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        /// MEAN: User already verify their phone
        if (authState.success && authState.code == null) {
            navHostController.navigate(Main)
        } else if (authState.error != null) {
            snackbarHostState.showSnackbar(
                "${authState.error}"
            )
        }
    }

    when {
        openAlertDialog -> {
            OpenTelegramDialog(
                onDismissRequest = { authViewModel.onOpenAlertDialogChange(!openAlertDialog) },
                onConfirmation = {
                    val redirectUrl = authState.redirectUrl
                    redirectUrl?.let {
                        TelegramLauncher.open(it)
                    }
                    authViewModel.onOpenAlertDialogChange(!openAlertDialog)
                    navHostController.navigate(OTP(phoneNumber = phoneNumberState.value))
                },
                dialogTitle = "Verify akun kamu!",
                dialogText = "Akun kamu belum terverifikasi di system. Silahkan klik tombol di bawah ini untuk membuka aplikasi Telegram",
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
                "Welcome to Community",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phoneNumberState.value,
                onValueChange = { text ->
                    authViewModel.onUpdatePhoneNumber(text)
                },
                isError = phoneNumberState.touched && phoneNumberState.error != null,
                supportingText = {
                    if (phoneNumberState.touched && phoneNumberState.error != null) {
                        Text(
                            text = phoneNumberState.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                },
                label = {
                    Text(
                        "Phone Number",
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                ),
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused && !phoneNumberState.touched) {
                            authViewModel.validatePhoneNumber(phoneNumberState.value)
                        }
                    },
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledIconButton(
                onClick = {
                    authViewModel.signIn()
                },
                enabled = !isLoadingState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isLoadingState) "Loading..." else if (hasAuthenticatedBefore) "Login" else "Send OTP",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}