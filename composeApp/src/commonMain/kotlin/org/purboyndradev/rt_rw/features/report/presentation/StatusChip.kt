package org.purboyndradev.rt_rw.features.report.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.data.dto.ReportStatus

@Composable
 fun StatusChip(status: ReportStatus) {
    val (bg, fg, label) = when (status) {
        ReportStatus.OPEN -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Open"
        )

        ReportStatus.IN_PROGRESS -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Sedang diproses"
        )

        ReportStatus.RESOLVED -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Selesai"
        )

        ReportStatus.CLOSED -> Triple(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Ditutup"
        )
    }

    Surface(
        color = bg,
        contentColor = fg,
        shape = RoundedCornerShape(999.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}