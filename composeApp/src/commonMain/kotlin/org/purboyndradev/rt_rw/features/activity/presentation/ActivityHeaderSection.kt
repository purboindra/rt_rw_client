package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.domain.model.ActivityDetailModel
import org.purboyndradev.rt_rw.helper.ActivityType
import org.purboyndradev.rt_rw.helper.DateHelper

@Composable
 fun ActivityHeaderSection(activity: ActivityDetailModel) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TypeChip(activity.type)
            Spacer(Modifier.width(8.dp))
            Text(
                text = DateHelper.convertEpochToDate(activity.date),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = activity.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = activity.activityId ?: "",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun TypeChip(type: ActivityType) {
    val label = when (type) {
        ActivityType.KEGIATAN_SOSIAL -> "Kegiatan Sosial"
        ActivityType.KERJA_BAKTI -> "Kerja Bakti"
        ActivityType.RAPAT-> "Rapat"
        ActivityType.RONDA -> "Ronda"
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
