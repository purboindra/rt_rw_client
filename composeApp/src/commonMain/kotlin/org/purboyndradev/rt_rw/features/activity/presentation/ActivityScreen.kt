package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
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
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(
                horizontal = 18.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Activities", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {}) {
                Icon(
                    FeatherIcons.Plus, contentDescription = "Add",
                    tint = Color.Black
                )
            }
        }
        when {
            activityState.loading -> {
                LoadingIndicatorCompose(modifier)
            }
            
            activityState.error != null -> {
                ErrorViewCompose(
                    modifier,
                    activityState.error ?: "Unknown Error"
                )
            }
            
            activityState.activities.isEmpty() -> {
                EmptyStateView(modifier, "No Activity Found")
            }
            
            else -> {
                ActivityList(modifier, activityState.activities)
            }
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
