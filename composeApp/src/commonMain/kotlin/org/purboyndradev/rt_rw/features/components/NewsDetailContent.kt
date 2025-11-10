package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
fun NewsDetailContent(news: NewsModel, modifier: Modifier = Modifier) {

    val timeAgo = DateHelper.formatIsoToTimeAgo(news.createdAt)

    Column {
        Text(
            news.title, style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(12.dp))
        Column {
            Text(news.author.name, style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
            ))
            Text(timeAgo, style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray
            ))
        }
        Spacer(modifier = modifier.height(12.dp))
        Text(news.description, style = MaterialTheme.typography.bodyMedium)
    }
}