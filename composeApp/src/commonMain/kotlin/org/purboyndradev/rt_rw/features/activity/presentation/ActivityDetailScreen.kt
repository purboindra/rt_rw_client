package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.purboyndradev.rt_rw.features.components.ActivityDetailContent
import org.purboyndradev.rt_rw.features.components.CommentInputActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(id: String, navHostController: NavHostController) {
    
    val activityViewModel =
        koinViewModel<ActivityViewModel>(parameters = { parametersOf(id) })
    val activityState by
    activityViewModel.activitiesState.collectAsStateWithLifecycle()
    val joinActivityState by activityViewModel.joinActivityState.collectAsStateWithLifecycle()
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(Unit) {
        activityViewModel.fetchActivityDetail(id)
    }
    
    LaunchedEffect(joinActivityState.error) {
        joinActivityState.error?.let {
            val errorMessage = joinActivityState.error
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage ?: "Unknown Error")
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBarCompose(navHostController, title = "Activity Detail")
        },
        bottomBar = {
            CommentInputActivity(
                modifier = Modifier,
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.padding(horizontal = 18.dp)
        ) {
            item {
                when (activityState.loading) {
                    true -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    
                    false -> if (activityState.error != null || activityState.activity == null) Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(activityState.error ?: "Unknown Error")
                    } else {
                        ActivityDetailContent(activityState.activity!!)
                    }
                }
            }
        }
    }
}