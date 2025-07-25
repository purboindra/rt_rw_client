package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel
import org.purboyndradev.rt_rw.helper.DateHelper


@Composable
fun ActivityDetailContent(
    activity: ActivityDetailModel,
    modifier: Modifier = Modifier
) {
    
    val date = DateHelper.convertEpochToDate(activity.date)
    
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = modifier.weight(1f)) {
                Text(
                    activity.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    date,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.Gray
                    )
                )
            }
            Spacer(modifier = modifier.width(8.dp))
            Box(modifier = modifier.width(112.dp)) {
                ElevatedButton(
                    onClick = {},
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text("Join", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
        
        Spacer(modifier = modifier.height(24.dp))
        Text(
            activity.description,
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                "${activity.users.size} partisipan",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
                )
            )
            Text(
                "Lihat Semua",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                ),
                modifier = modifier.clickable { }
            )
        }
        Text(activity.pic.name)
    }
}