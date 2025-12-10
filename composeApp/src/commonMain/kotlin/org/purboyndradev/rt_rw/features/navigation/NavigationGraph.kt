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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlinx.serialization.json.Json
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityDetailScreen
import org.purboyndradev.rt_rw.features.auth.presentation.LoginScreen
import org.purboyndradev.rt_rw.features.auth.presentation.OTPScreen
import org.purboyndradev.rt_rw.features.auth.presentation.VerifyEmailOnboardingScreen
import org.purboyndradev.rt_rw.features.components.CameraPreviewScreen
import org.purboyndradev.rt_rw.features.main.presentation.MainScreen
import org.purboyndradev.rt_rw.features.news.presentation.NewsDetailScreen
import org.purboyndradev.rt_rw.features.notification.NotificationOnboardingScreen
import org.purboyndradev.rt_rw.features.report.presentation.CreateReportScreen
import org.purboyndradev.rt_rw.features.report.presentation.ReportDetailScreen
import org.purboyndradev.rt_rw.features.report.presentation.ReportsScreen
import org.purboyndradev.rt_rw.features.splash.SplashScreen
import org.purboyndradev.rt_rw.helper.OTPType

data class StartDestinationData(
    val route: String,
    val data: Any? = null
)

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {

    override fun put(bundle: SavedState, key: String, value: T) {
        bundle.write { putString(key, json.encodeToString(value)) }
    }

    override fun get(bundle: SavedState, key: String): T? {
        return json.decodeFromString<T?>(bundle.read { getString(key) })
    }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)
}


@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    startDestination: StartDestinationData? = null
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Splash,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(navHostController = navController)
        }
        composable<NewsDetail> { backStackEntry ->
            val activityDetail = backStackEntry.toRoute<ActivityDetail>()
            val id = activityDetail.id
            NewsDetailScreen(navHostController = navController, id = id)
        }
        composable (
            arguments = listOf(
                navArgument("otpType") {
                    type = serializableType<OTPType>()
                    defaultValue = OTPType.TELEGRAM
                }
            ),

            route = OTP.serializer().descriptor.serialName,
        ) { backStackEntry ->
            val otpScreen = backStackEntry.toRoute<OTP>()
            val phoneNumber = otpScreen.phoneNumber
            val otpType = otpScreen.otpType
            val email = otpScreen.email

            OTPScreen(
                navHostController = navController,
                phoneNumber = phoneNumber,
                otpType = otpType,
                email = email
            )
        }
        composable<Splash> {
            SplashScreen(navHostController = navController, startDestination)
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
        composable<NotificationPermissions> {
            NotificationOnboardingScreen(navHostController = navController)
        }
        composable<CreateReport> {
            CreateReportScreen(navHostController = navController)
        }
        composable<CameraPreviewScreen> {
            CameraPreviewScreen(navHostController = navController)
        }
        composable<VerifyEmailOnBoardingScreen> {
            VerifyEmailOnboardingScreen(navController)
        }
        composable<Reports> {
            ReportsScreen(navController)
        }
        composable<ReportDetail> { backStackEntry ->
            val reportDetail = backStackEntry.toRoute<ReportDetail>()
            val id = reportDetail.id
            ReportDetailScreen(id, navController)
        }
    }
}