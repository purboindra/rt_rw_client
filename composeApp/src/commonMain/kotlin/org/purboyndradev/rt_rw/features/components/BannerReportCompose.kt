package org.purboyndradev.rt_rw.features.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BannerReportCompose(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.height(124.dp)
                .width(84.dp).clip(
                    RoundedCornerShape(
                        size = 14.dp
                    )
                ).background(Color.LightGray)
        )
        
        Spacer(modifier = modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Lapor Kejadian",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                "Ada kejadian di sekitar? Laporkan sekarang supaya pengurus bisa segera tindak lanjuti dan pastikan lingkungan kita tetap aman.",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.width(124.dp),
                shape = RoundedCornerShape(
                    size = 8.dp
                )
            ) {
                Text(
                    "Laporkan",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}