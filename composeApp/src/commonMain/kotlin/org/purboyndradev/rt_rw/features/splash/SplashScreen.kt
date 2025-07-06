package org.purboyndradev.rt_rw.features.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.navigation.Login
import org.purboyndradev.rt_rw.features.navigation.OTP

@Composable
fun SplashScreen(navHostController: NavHostController) {
    
    val splashViewModel = koinViewModel<SplashViewModel>()
    val splashNavigationState =
        splashViewModel.navigationState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        splashViewModel.refreshToken()
    }
    
    LaunchedEffect(splashNavigationState.value) {
        when (splashNavigationState.value) {
            is SplashNavigationState.NavigateToLogin -> {
                navHostController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
            
            is SplashNavigationState.NavigateToHome -> {
                navHostController.navigate(OTP) {
                    popUpTo(0) { inclusive = true }
                }
            }
            
            else -> Unit
        }
    }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(
                horizontal = 12.dp,
                vertical = 18.dp
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to RT RW Apps")
        }
    }
}