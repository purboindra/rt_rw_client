package org.purboyndradev.rt_rw.features.report.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
 fun ReportDescriptionSection(description: String) {
    SectionCard(
        title = "Deskripsi Laporan"
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
