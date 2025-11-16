package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import org.purboyndradev.rt_rw.core.domain.model.ActivityModel

@Composable
fun ActivityCompose(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: String? = null,
    activities: List<ActivityModel>,
    onActivityTapped: (id: String) -> Unit
) {
    Column {
        Text(
            "Activity", style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(8.dp))
        when {
            isLoading -> Box(
                modifier = modifier.height(82.dp).width(82.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error != null -> Box(
                modifier = modifier.height(82.dp).wrapContentWidth(),
            ) {
                Text(error)
            }

            activities.isEmpty() -> Box(
                modifier = modifier.height(82.dp).wrapContentWidth(),
            ) {
                Text("No Activity")
            }

            else -> ActivityLazyRow(
                activities,
                onActivityTapped
            )
        }
    }
}


@Composable
private fun ActivityLazyRow(
    activities: List<ActivityModel>,
    onActivityTapped: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(activities, key = { it.id }) { activity ->
            Box(
                modifier = Modifier
                    .height(102.dp)
                    .width(184.dp)
                    .padding(horizontal = 5.dp)
                    .clip(RoundedCornerShape(size = 16.dp))
                    .background(Color.LightGray.copy(alpha = 0.35f))
                    .clickable { onActivityTapped(activity.id) },
                contentAlignment = Alignment.Center
            ) {
                if (activity.bannerImageUrl == null) Icon(
                    imageVector = FeatherIcons.Info,
                    contentDescription = activity.title,
                    modifier = Modifier.fillMaxWidth(),
                ) else AsyncImage(
                    model = activity.bannerImageUrl,
                    contentScale = ContentScale.Crop,
                    contentDescription = activity.title,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}