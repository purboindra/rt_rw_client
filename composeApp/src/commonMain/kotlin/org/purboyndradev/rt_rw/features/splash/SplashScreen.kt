package org.purboyndradev.rt_rw.features.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
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
import org.purboyndradev.rt_rw.features.navigation.ActivityDetail
import org.purboyndradev.rt_rw.features.navigation.Login
import org.purboyndradev.rt_rw.features.navigation.Main
import org.purboyndradev.rt_rw.features.navigation.StartDestinationData

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    startDestination: StartDestinationData? = null
) {
    
    val splashViewModel = koinViewModel<SplashViewModel>()
    val splashNavigationState by
    splashViewModel.navigationState.collectAsStateWithLifecycle()
    val isLoading by splashViewModel.isLoading.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        splashViewModel.refreshToken()
    }
    
    LaunchedEffect(
        splashNavigationState,
        isLoading
    
    ) {
        if (!isLoading) when (splashNavigationState) {
            is SplashNavigationState.NavigateToLogin -> {
                navHostController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
            
            is SplashNavigationState.NavigateToHome -> {
                navHostController.navigate(Main) {
                    popUpTo(0) { inclusive = true }
                }
            }
            
            is SplashNavigationState.NavigateToActivity -> {
                val itemId =
                    (splashNavigationState as SplashNavigationState.NavigateToActivity).activityId
                navHostController.navigate(ActivityDetail(itemId)) {
                    popUpTo(0) { inclusive = true }
                }
            }
            
            else -> navHostController.navigate(Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Welcome to RT RW Apps")
                Spacer(modifier = Modifier.width(12.dp))
                CircularProgressIndicator()
            }
        }
    }
}