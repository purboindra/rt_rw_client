package org.purboyndradev.rt_rw.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.purboyndradev.rt_rw.features.auth.presentation.LoginScreen
import org.purboyndradev.rt_rw.features.auth.presentation.OTPScreen
import org.purboyndradev.rt_rw.features.main.presentation.MainScreen
import org.purboyndradev.rt_rw.features.splash.SplashScreen

@Composable
fun NavigationGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Splash,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(navHostController = navController)
        }
        composable<OTP> { backStackEntry ->
            val otpScreen = backStackEntry.toRoute<OTP>()
            val phoneNumber = otpScreen.phoneNumber
            OTPScreen(
                navHostController = navController,
                phoneNumber = phoneNumber
            )
        }
        composable<Splash> {
            SplashScreen(navHostController = navController)
        }
        composable<Main> {
            MainScreen(navHostController = navController)
        }
    }
}