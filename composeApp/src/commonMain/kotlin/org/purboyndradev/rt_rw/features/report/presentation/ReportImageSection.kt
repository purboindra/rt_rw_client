package org.purboyndradev.rt_rw.features.report.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
 fun ReportImageSection(
    imageUrl: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        tonalElevation = 2.dp
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Report image",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 180.dp)
                .aspectRatio(16f / 9f)
                .clickableWithoutRipple(onClick),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier =
    this.clickable(
        indication = null,
        interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource(),
        role = Role.Button,
        onClick = onClick
    )