package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeaderProfile(
    modifier: Modifier = Modifier,
    userName: String,
    email: String? = null,
    phoneNumber: String,
) {
    Row(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = modifier.size(64.dp).clip(
                shape = RoundedCornerShape(100)
            ).background(
                color = Color.Gray
            )
        )
        Spacer(modifier = modifier.width(8.dp))
        Column {
            Text(
                userName, style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                )
            )
            Spacer(modifier = modifier.height(2.dp))
            if (email != null) Text(
                email, style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                )
            )
            Spacer(modifier = modifier.height(2.dp))
            Text(
                phoneNumber, style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                )
            )
        }
    }
}