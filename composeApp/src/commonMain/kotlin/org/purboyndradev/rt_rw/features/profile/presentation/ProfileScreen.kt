package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.navigation.Login


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    userName: String,
) {
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val logOutState by profileViewModel.logOutState.collectAsStateWithLifecycle()

    LaunchedEffect(logOutState.success) {
        if (logOutState.success) {
            navHostController.navigate(Login)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hello $userName")
        FilledIconButton(
            modifier = modifier.fillMaxWidth(),
            onClick = {
                profileViewModel.logout()
            }, enabled = !logOutState.loading
        ) {
            Text(if (logOutState.loading) "Loading..." else "Logout")
        }
    }
}