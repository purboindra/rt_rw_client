package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.purboyndradev.rt_rw.core.domain.model.BannerModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerHomeCompose(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    error: String? = null,
    banners: List<BannerModel>,
) {

    if (isLoading) Box(
        modifier = modifier.height(150.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    } else if (error != null) {
        Box(
            modifier = modifier.height(150.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Error: $error",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else if (banners.isEmpty()) {
        Box(
            modifier = modifier.height(150.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "No banners to display.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        HorizontalMultiBrowseCarousel(
            state = rememberCarouselState { banners.count() },
            modifier = modifier.fillMaxWidth().wrapContentHeight().padding(
                top = 16.dp, bottom = 16.dp
            ),
            preferredItemWidth = 420.dp,
            itemSpacing = 8.dp,
        ) { i ->
            val banner = banners[i]
            Box(
                modifier = Modifier.height(148.dp).width(320.dp).clip(
                    RoundedCornerShape(
                        size = 16.dp
                    )
                ).background(
                    Color.LightGray
                ),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = banner.imageUrl,
                    contentDescription = banner.title,
                    modifier = Modifier.fillMaxWidth().height(148.dp).fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                )
                Text(
                    banner.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}