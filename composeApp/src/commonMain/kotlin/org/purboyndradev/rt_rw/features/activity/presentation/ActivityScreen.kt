package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.features.components.EmptyStateView
import org.purboyndradev.rt_rw.features.components.ErrorViewCompose
import org.purboyndradev.rt_rw.features.components.LoadingIndicatorCompose

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val activityViewModel = koinViewModel<ActivityViewModel>(parameters = {
        parametersOf("1")
    })
    val activityState by activityViewModel.activitiesState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        activityViewModel.fetchActivities()
    }
    
    when {
        activityState.loading -> {
            LoadingIndicatorCompose(modifier)
        }
        
        activityState.error != null -> {
            ErrorViewCompose(modifier, activityState.error ?: "Unknown Error")
        }
        
        activityState.activities.isEmpty() -> {
            EmptyStateView(modifier, "No Activity Found")
        }
        
        else -> {
            ActivityList(modifier, activityState.activities)
        }
    }
}

@Composable
private fun ActivityList(
    modifier: Modifier = Modifier,
    activities: List<ActivityModel>
) {
    LazyColumn(modifier = modifier) {
        items(activities) { activity ->
            Text(
                "Activity: ${activity.title}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
