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
import androidx.compose.ui.unit.dp

data class CarouselItem(
    val id: Int,
    val contentDescription: String,
    val imageUrl: String? = null,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerHomeCompose(
    modifier: Modifier = Modifier,
) {

    val items = remember {
        listOf(
            CarouselItem(0, "cupcake"),
            CarouselItem(1, "donut"),
            CarouselItem(2, "eclair"),
            CarouselItem(3, "froyo"),
            CarouselItem(4, "gingerbread"),
        )
    }

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.count() },
        modifier = modifier.fillMaxWidth().wrapContentHeight().padding(
            top = 16.dp, bottom = 16.dp
        ),
        preferredItemWidth = 250.dp,
        itemSpacing = 8.dp,
    ) { i ->
        val item = items[i]
        Box(
            modifier = Modifier.height(148.dp).width(250.dp).clip(
                RoundedCornerShape(
                    size = 16.dp
                )
            ).background(
                Color.LightGray
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(item.contentDescription)
        }
    }

}