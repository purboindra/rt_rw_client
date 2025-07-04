package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.TelegramLauncher
import org.purboyndradev.rt_rw.features.components.OpenTelegramDialog
import org.purboyndradev.rt_rw.features.navigation.OTP

@Composable
fun LoginScreen(navHostController: NavHostController) {
    
    val authViewModel = koinViewModel<AuthViewModel>()
    
    val phoneNumberState =
        authViewModel.phoneNumberState.collectAsStateWithLifecycle()
    val isLoadingState =
        authViewModel.isLoadingState.collectAsStateWithLifecycle()
    val authState = authViewModel.signInState.collectAsStateWithLifecycle()
    val openAlertDialog =
        authViewModel.openAlertDialog.collectAsStateWithLifecycle()
    
    when {
        openAlertDialog.value -> {
            OpenTelegramDialog(
                onDismissRequest = { authViewModel.onOpenAlertDialogChange(!openAlertDialog.value) },
                onConfirmation = {
                    val redirectUrl = authState.value.redirectUrl
                    redirectUrl?.let {
                        TelegramLauncher.open(it)
                    }
                    authViewModel.onOpenAlertDialogChange(!openAlertDialog.value)
                    navHostController.navigate(OTP(phoneNumber = authViewModel.phoneNumberState.value))
                },
                dialogTitle = "Verify akun kamu!",
                dialogText = "Akun kamu belum terverifikasi di system. Silahkan klik tombol di bawah ini untuk membuka aplikasi Telegram",
            )
        }
    }
    
    Scaffold { paddingValues ->
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
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            ElevatedButton(
                onClick = {
                    authViewModel.signIn()
                },
                enabled = !isLoadingState.value
            ) {
                Text(
                    if (isLoadingState.value) "Loading..." else "Send OTP",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}