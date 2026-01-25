package org.purboyndradev.rt_rw.features.profile.presentation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.components.EmptyStateView
import org.purboyndradev.rt_rw.features.components.ErrorViewCompose
import org.purboyndradev.rt_rw.features.components.LoadingIndicatorCompose
import org.purboyndradev.rt_rw.features.navigation.Login

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val logOutState by profileViewModel.logOutState.collectAsStateWithLifecycle()
    val currentUserState by profileViewModel.currentUserState.collectAsStateWithLifecycle()

    LaunchedEffect(logOutState.success) {
        if (logOutState.success) {
            navHostController.navigate(Login)
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchCurrentUser()
    }

    when {
        currentUserState.loading -> {
            LoadingIndicatorCompose(modifier)
        }

        currentUserState.error != null -> {
            ErrorViewCompose(
                modifier,
                currentUserState.error ?: "Unknown Error"
            )
        }

        currentUserState.data != null -> {

            val user = currentUserState.data!!

            ProfileContent(
                modifier = modifier.fillMaxSize(),
                userName = user.name,
                email = user.email
            )
        }

        else -> {
            EmptyStateView(message = "Tidak ada data")
        }
    }

}