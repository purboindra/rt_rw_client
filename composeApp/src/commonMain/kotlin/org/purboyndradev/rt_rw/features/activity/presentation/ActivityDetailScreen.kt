package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Send
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
    
    LaunchedEffect(Unit) {
        activityViewModel.fetchActivityDetail()
    }
    
    Scaffold(
        topBar = {
            TopAppBarCompose(navHostController, title = "Activity Detail")
        },
        bottomBar = {
            CommentInputActivity(
                modifier = Modifier,
            )
        }
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