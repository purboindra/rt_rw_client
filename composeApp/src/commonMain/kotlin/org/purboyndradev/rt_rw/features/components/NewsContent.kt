package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.domain.model.NewsModel
import org.purboyndradev.rt_rw.helper.DateHelper

@Composable
fun NewsContent(news: NewsModel, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.padding(
            vertical = 8.dp
        )
    ) {
        Text(
            text = news.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            )
        )
        Text(
            text = news.description,
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color.Gray,
            ),
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(
                modifier = modifier.weight(1f)
            )
            Text(
                text = DateHelper.formatIsoToDate(news.createdAt),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                ),
            )
        }
    }
}