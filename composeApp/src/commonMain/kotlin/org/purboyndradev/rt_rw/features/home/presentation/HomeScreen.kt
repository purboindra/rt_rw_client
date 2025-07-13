package org.purboyndradev.rt_rw.features.home.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.purboyndradev.rt_rw.features.activity.presentation.ActivityState
import org.purboyndradev.rt_rw.features.components.ActivityCompose
import org.purboyndradev.rt_rw.features.components.BannerHomeCompose
import org.purboyndradev.rt_rw.features.components.BannerReportCompose
import org.purboyndradev.rt_rw.features.components.HeaderUserCompose
import org.purboyndradev.rt_rw.features.components.NewsCompose
import org.purboyndradev.rt_rw.features.components.UnRegisterEmailCompose
import org.purboyndradev.rt_rw.features.navigation.ActivityDetail

@Preview
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    activityState: ActivityState
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 18.dp)
    ) {
        item {
            HeaderUserCompose(
                modifier
            )
            /// Banner
            BannerHomeCompose(
                modifier = modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(22.dp))
            /// Banner Unregistered Email
            UnRegisterEmailCompose(
                modifier
            )
            Spacer(modifier = Modifier.height(22.dp))
            /// Activity
            ActivityCompose(
                modifier = Modifier.fillMaxWidth(),
                isLoading = activityState.loading,
                error = activityState.error,
                activities = activityState.activities,
                onActivityTapped = {
                    navHostController.navigate(ActivityDetail(id = it))
                }
            )
            Spacer(modifier = Modifier.height(22.dp))
            /// Banner Report Kejadian
            BannerReportCompose()
            Spacer(modifier = Modifier.height(22.dp))
            /// Activity
            NewsCompose(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}