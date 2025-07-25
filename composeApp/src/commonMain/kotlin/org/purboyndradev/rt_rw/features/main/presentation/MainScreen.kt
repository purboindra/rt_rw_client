package org.purboyndradev.rt_rw.features.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MainScreen(navHostController: NavHostController) {
    
    val mainViewModel = koinViewModel<MainViewModel>()
    val activityState =
        mainViewModel.activitiesState.collectAsStateWithLifecycle()
    
    val bottomNavigationController = rememberNavController()
    val currentDestination =
        bottomNavigationController.currentBackStackEntryAsState().value?.destination?.route
            ?: Home.ROUTE
    
    Scaffold(
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
        NavHost(
            navController = bottomNavigationController,
            startDestination = currentDestination,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable(route = Home.ROUTE) {
                HomeScreen(
                    navHostController = navHostController,
                    activityState = activityState.value
                )
            }
            composable(route = Activity.ROUTE) {
                ActivityScreen(
                    navHostController = navHostController
                )
            }
            composable(route = News.ROUTE) {
                NewsScreen(
                    navHostController = navHostController
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
