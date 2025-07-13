package org.purboyndradev.rt_rw.features.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.purboyndradev.rt_rw.features.components.ActivityDetailScreen
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
        composable<Home> {
            Scaffold { innerPadding ->
                Column(
                    modifier = modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Home Screen")
                }
            }
        }
        composable<Profile> {
            Scaffold { innerPadding ->
                Column(
                    modifier = modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Profile Screen")
                }
            }
        }
        composable<News> {
            Scaffold { innerPadding ->
                Column(
                    modifier = modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("News Screen")
                }
            }
        }
        composable<Activity> {
            Scaffold { innerPadding ->
                Column(
                    modifier = modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Activity Screen")
                }
            }
        }
        composable<ActivityDetail> { backStackEntry ->
            val activityDetail = backStackEntry.toRoute<ActivityDetail>()
            val id = activityDetail.id
            ActivityDetailScreen(id, navHostController = navController)
        }
    }
}