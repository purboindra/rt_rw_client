package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityViewModel
import org.purboyndradev.rt_rw.features.activity.presentation.TopAppBarCompose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(id: String, navHostController: NavHostController) {
    
    val activityViewModel =
        koinViewModel<ActivityViewModel>(parameters = { parametersOf(id) })
    val activityState =
        activityViewModel.activitiesState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBarCompose(navHostController, title = "Activity Detail")
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
        ) {
            item {
                when (activityState.value.loading) {
                    true -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    
                    false -> if (activityState.value.error != null) Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(activityState.value.error!!)
                    } else {
                        Text(activityState.value.activities.firstOrNull()?.title?:"No Activity")
                    }
                }
            }
        }
    }
}