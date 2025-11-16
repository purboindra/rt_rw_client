package org.purboyndradev.rt_rw.features.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityScreen
import org.purboyndradev.rt_rw.features.components.BottomNavItem
import org.purboyndradev.rt_rw.features.home.presentation.HomeScreen
import org.purboyndradev.rt_rw.features.navigation.Activity
import org.purboyndradev.rt_rw.features.navigation.Home
import org.purboyndradev.rt_rw.features.navigation.News
import org.purboyndradev.rt_rw.features.navigation.Profile
import org.purboyndradev.rt_rw.features.news.presentation.NewsScreen
import org.purboyndradev.rt_rw.features.profile.presentation.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navHostController: NavHostController) {

    val mainViewModel = koinViewModel<MainViewModel>()
    val activityState by
    mainViewModel.activitiesState.collectAsStateWithLifecycle()
    val loadingState by mainViewModel.loadingState.collectAsStateWithLifecycle()
    val bannerState by mainViewModel.bannersState.collectAsStateWithLifecycle()
    val newsState by mainViewModel.newsState.collectAsStateWithLifecycle()
    val userName by mainViewModel.userNameFlow.collectAsStateWithLifecycle()
    val email by mainViewModel.emailFlow.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val bottomNavigationController = rememberNavController()
    val currentDestination =
        bottomNavigationController.currentBackStackEntryAsState().value?.destination?.route
            ?: Home.ROUTE
    val backStackEntry = navHostController.currentBackStackEntry

    LaunchedEffect(Unit) {
        mainViewModel.onInit()
    }

    LaunchedEffect(backStackEntry) {
        backStackEntry?.savedStateHandle?.get<String>("snackbar_message")?.let { message ->
            snackbarHostState.showSnackbar(
                message
            )
            backStackEntry.savedStateHandle.remove<String>("snackbar_message")
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent,
            ) {
                BottomNavItem.items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentDestination == item.route,

                        onClick = {
                            bottomNavigationController.navigate(
                                item.route
                            ) {
                                popUpTo(
                                    bottomNavigationController.graph.startDestinationId
                                ) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            item.label
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = loadingState,
            onRefresh = {
                scope.launch {
                    mainViewModel.onInit()
                }
            },
            state = pullToRefreshState,

            ) {
            NavHost(
                navController = bottomNavigationController,
                startDestination = currentDestination,
                modifier = Modifier.padding(innerPadding).fillMaxSize()
            ) {
                composable(route = Home.ROUTE) {
                    HomeScreen(
                        navHostController = navHostController,
                        activityState = activityState,
                        bannerState = bannerState,
                        newsState = newsState,
                        userName = userName ?: "",
                        isEmailEmpty = email.isNullOrBlank()
                    )
                }
                composable(route = Activity.ROUTE) {
                    ActivityScreen(
                        navHostController = navHostController
                    )
                }
                composable(route = News.ROUTE) {
                    NewsScreen(
                        navHostController = navHostController,
                        itemId = 1
                    )
                }
                composable(route = Profile.ROUTE) {
                    ProfileScreen(
                        navHostController = navHostController,
                    )
                }
            }
        }
    }
}
