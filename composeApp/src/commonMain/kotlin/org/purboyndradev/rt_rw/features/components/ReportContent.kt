package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.domain.model.ReportModel
import org.purboyndradev.rt_rw.helper.DateHelper


@Composable
fun ReportContent(
    report: ReportModel, modifier: Modifier = Modifier,
    onReportTapped: (id: String) -> Unit
) {
    Column(
        modifier = modifier.padding(
            vertical = 8.dp
        ).clickable {
            onReportTapped(report.id)
        }
    ) {
        Text(
            text = report.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(
            text = report.description,
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color.Gray,
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "by ${report.user.name}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = DateHelper.formatIsoToDate(report.createdAt),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                ),
            )
        }
    }
}