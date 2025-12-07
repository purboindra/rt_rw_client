package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.purboyndradev.rt_rw.features.components.SectionCard
import org.purboyndradev.rt_rw.helper.MessageSnackbarType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailScreen(
    id: String, navHostController: NavHostController
) {

    val activityViewModel = koinViewModel<ActivityViewModel>(parameters = { parametersOf(id) })
    val activityState by activityViewModel.activitiesState.collectAsStateWithLifecycle()
    val joinActivityState by activityViewModel.joinActivityState.collectAsStateWithLifecycle()
    val snackbarTypeState by activityViewModel.snackbarType.collectAsStateWithLifecycle()
    val hasJoinActivity by activityViewModel.hasJoinedActivity.collectAsStateWithLifecycle()
    val usersActivityState by activityViewModel.usersActivityState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val openUsersDialog = remember { mutableStateOf(false) }

    val isLoadingJoinActivity = joinActivityState.loading

    fun showStyledSnackbar(
        snackbarHostState: SnackbarHostState,
        scope: CoroutineScope,
        message: String,
        type: MessageSnackbarType
    ) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = type.name,
                duration = if (snackbarTypeState == MessageSnackbarType.SUCCESS) SnackbarDuration.Short else SnackbarDuration.Long
            )
        }
    }

    LaunchedEffect(Unit) {
        activityViewModel.fetchActivityDetail(id)
    }

    Scaffold(topBar = {
//            SmallTopAppBar(
//                title = {
//                    Text(
//                        "Activity Detail",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
    }, bottomBar = {
//            if (onSendMessageClick != {} && onMessageTextChange != {}) {
//                ActivityDiscussionBar(
//                    text = messageText,
//                    onTextChange = onMessageTextChange,
//                    onSend = onSendMessageClick
//                )
//            }
        ActivityDiscussionBar(text = "messageText", onTextChange = {}, onSend = {})
    }) { innerPadding ->

        when {
            activityState.loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            activityState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(activityState.error!!)
                }
            }

            activityState.activity != null -> {
                val activity = activityState.activity
                LazyColumn(
                    modifier = Modifier.padding(innerPadding).fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ActivityHeaderSection(activity!!)
                    }

                    item {
                        ActivityBannerSection(
                            bannerUrl = activity!!.bannerImageUrl ?: activity.imageUrl,
                            onClick = {})
                    }

                    item {
                        SectionCard(title = "Deskripsi") {
                            Text(
                                activity!!.description, style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    item {
                        PicAndRtSection(
                            picName = activity!!.pic.name,
                            picRole = activity.pic.role,
                            rtName = activity.pic.rt?.name ?: "",
                            rtAddress = activity.rt?.address ?: "",
                            createdByName = activity.createdBy.name,
                            onPicClick = { })
                    }

                    item {
                        ParticipantsSection(
                            participants = activity!!.users, onSeeAllClick = {})
                    }

//            item {
//                DiscussionSection(
//                    text = messageText,
//                    onTextChange = onMessageTextChange,
//                    onSend = onSendMessageClick
//                )
//            }
//
                }
            }

            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Activity not found")
                }
            }
        }
    }
}
