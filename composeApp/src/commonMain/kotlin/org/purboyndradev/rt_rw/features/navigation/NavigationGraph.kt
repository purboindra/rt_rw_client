package org.purboyndradev.rt_rw.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.purboyndradev.rt_rw.features.auth.presentation.LoginScreen
import org.purboyndradev.rt_rw.features.auth.presentation.OTPScreen

@Composable
fun NavigationGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController, startDestination = Login,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(navHostController = navController)
        }
        composable<OTP> {
            OTPScreen(navHostController = navController)
        }
    }
}