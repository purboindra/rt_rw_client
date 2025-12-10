package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.features.components.EmptyStateView
import org.purboyndradev.rt_rw.features.components.ErrorViewCompose
import org.purboyndradev.rt_rw.features.components.LoadingIndicatorCompose
import org.purboyndradev.rt_rw.features.navigation.ActivityDetail

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val activityViewModel = koinViewModel<ActivityViewModel>()
    val activityState by activityViewModel.activitiesState.collectAsStateWithLifecycle()

    val searchQuery by activityViewModel.searchQuery.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

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


        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                activityViewModel.onQueryChange(it)
            },
            modifier = modifier.fillMaxWidth().padding(
                horizontal = 18.dp
            ),
            placeholder = {
                Text("Search")
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                    val queryParams = QueryParams(
                        query = searchQuery
                    )

                    activityViewModel.fetchActivities(queryParams)

                    keyboardController?.hide()
                }
            )
        )

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

            activityState.activities.isNotEmpty() -> {
                val activities = activityState.activities
                ActivityList(
                    modifier = modifier.fillMaxSize().padding(
                        horizontal = 12.dp,
                    ), activities, onActivityTapped = {
                        navHostController.navigate(ActivityDetail(it))
                    })
            }

            else -> {
                EmptyStateView(modifier, "Tidak ada aktifitas")
            }
        }
    }
}

@Composable
private fun ActivityList(
    modifier: Modifier = Modifier,
    activities: List<ActivityModel>,
    onActivityTapped: (id: String) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(activities) { activity ->
            ActivityContent(activity, onActivityTapped = onActivityTapped)
        }
    }
}
