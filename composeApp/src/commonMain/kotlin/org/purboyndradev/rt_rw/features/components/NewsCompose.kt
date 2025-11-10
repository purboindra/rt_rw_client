package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.purboyndradev.rt_rw.core.domain.model.NewsModel


@Composable
fun NewsCompose(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: String? = null,
    news: List<NewsModel>,
    onNewsTapped: (id: String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            "News", style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(8.dp))
        Box(
            modifier = Modifier.height(228.dp).fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            } else if (error != null) {
                Text(error, Modifier.align(Alignment.Center))
            } else if (news.isEmpty()) {
                Text("No News...", Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxHeight(), userScrollEnabled = false
                ) {
                    itemsIndexed(
                        items = news,
                        key = { _, newsItem -> "indexed-${newsItem.id}" }) { index, newsItem ->

                        NewsContent(
                            news = newsItem, modifier = modifier,
                            onNewsTapped = onNewsTapped
                        )

                        if (index < news.size - 1) {
                            HorizontalDivider(
                                Modifier.padding(horizontal = 1.dp), 1.dp, Color.LightGray
                            )
                        }
                    }

                }
            }
        }
    }
}