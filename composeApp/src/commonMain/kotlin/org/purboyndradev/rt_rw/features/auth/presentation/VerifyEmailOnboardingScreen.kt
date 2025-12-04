package org.purboyndradev.rt_rw.features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.CheckCircle
import compose.icons.feathericons.Key
import compose.icons.feathericons.Mail
import compose.icons.feathericons.Shield
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.components.AddVerifyEmailDialog
import org.purboyndradev.rt_rw.features.navigation.Main
import org.purboyndradev.rt_rw.features.navigation.OTP
import org.purboyndradev.rt_rw.helper.OTPType

@Composable
fun VerifyEmailOnboardingScreen(
    navHostController: NavHostController,
) {

    val authViewModel = koinViewModel<AuthViewModel>()
    val requestEmailVerificationState by authViewModel.requestEmailVerificationState.collectAsStateWithLifecycle()
    val openAddEmailDialog by authViewModel.openAddEmailDialog.collectAsStateWithLifecycle()
    val emailState by authViewModel.emailState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    val isLoadingRequestEmail = requestEmailVerificationState.isLoading

    LaunchedEffect(requestEmailVerificationState.error, requestEmailVerificationState.success) {

        if (!requestEmailVerificationState.error.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = requestEmailVerificationState.error!!,
            )
        } else if (requestEmailVerificationState.success) {
            navHostController.navigate(OTP(otpType = OTPType.EMAIL, email = emailState.value))
            authViewModel.onResetRequestEmailVerificationState()
        }
    }

    when {
        openAddEmailDialog -> {
            AddVerifyEmailDialog(
                onAddEmail = {
                    authViewModel.onOpenAddEmailDialogChange(!openAddEmailDialog)
                    authViewModel.requestEmailVerification()
                },
                onValueChange = {
                    authViewModel.updateEmailValue(it)
                },
                email = emailState.value,
                onDismissRequest = {
                    authViewModel.onOpenAddEmailDialogChange(!openAddEmailDialog)
                }
            )
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = FeatherIcons.Mail,
                contentDescription = "Verify Email",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Keep Your Account Secure",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Verifying your email helps us keep your account secure and ensures you can recover it if you ever lose access. You'll also receive important community updates.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Column(
                modifier = Modifier.padding(bottom = 48.dp)
            ) {
                BenefitItem(
                    icon = FeatherIcons.Shield,
                    title = "Enhanced Account Security",
                    description = "Protect your account from unauthorized access."
                )

                Spacer(modifier = Modifier.height(16.dp))

                BenefitItem(
                    icon = FeatherIcons.Key,
                    title = "Easy Account Recovery",
                    description = "Quickly regain access if you forget your password."
                )

                Spacer(modifier = Modifier.height(16.dp))

                BenefitItem(
                    icon = FeatherIcons.CheckCircle,
                    title = "Receive Important Updates",
                    description = "Stay informed about neighborhood news and activities."
                )
            }

            Button(
                onClick = {
                    authViewModel.onOpenAddEmailDialogChange(!openAddEmailDialog)
                },
                enabled = !isLoadingRequestEmail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = FeatherIcons.Mail,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isLoadingRequestEmail) "Loading..." else "Add/Verify Your Email",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            TextButton(
                onClick = {
                    navHostController.navigate(Main) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    text = "Maybe Later",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "You can change these settings anytime in the app settings.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun BenefitItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
