package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel

@Composable
fun ActivityDetailContent(
    activityModel: ActivityModel,
    modifier: Modifier = Modifier
) {
    Column {
        Text(activityModel.title)
        Text(activityModel.description)
        Text(activityModel.pic.username ?: "-")
    }
}