package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ActivityCompose(modifier: Modifier = Modifier) {
    Column {
        Text(
            "Activity", style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(modifier = modifier.height(8.dp))
        Box(
            modifier = Modifier.wrapContentHeight()
        ) {
            LazyRow(
            ) {
                items(10) {
                    Box(
                        modifier = Modifier.height(102.dp).width(184.dp).padding(horizontal = 5.dp).clip(
                            RoundedCornerShape(size = 16.dp)
                        ).background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Hello World: $it")
                    }
                }
            }
        }
    }
}