package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertCircle
import compose.icons.feathericons.CheckCircle
import org.purboyndradev.rt_rw.helper.MessageSnackbarType

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
    messageType: MessageSnackbarType
) {
    val backgroundColor = when (messageType) {
        MessageSnackbarType.SUCCESS -> Color(0xFF4CAF50)
        MessageSnackbarType.ERROR -> Color(0xFFF44336)
        MessageSnackbarType.INFO -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (messageType) {
        MessageSnackbarType.SUCCESS -> Color.White
        MessageSnackbarType.ERROR -> Color.White
        MessageSnackbarType.INFO -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    // You could also use MaterialTheme.colorScheme for more theme-consistent colors
    // val backgroundColor = when (messageType) {
    //     MessageType.SUCCESS -> MaterialTheme.colorScheme.tertiaryContainer
    //     MessageType.ERROR -> MaterialTheme.colorScheme.errorContainer
    //     MessageType.INFO -> MaterialTheme.colorScheme.surfaceVariant
    // }
    // val contentColor = when (messageType) {
    //     MessageType.SUCCESS -> MaterialTheme.colorScheme.onTertiaryContainer
    //     MessageType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
    //     MessageType.INFO -> MaterialTheme.colorScheme.onSurfaceVariant
    // }
    
    
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (messageType) {
                MessageSnackbarType.SUCCESS -> FeatherIcons.CheckCircle
                MessageSnackbarType.ERROR -> FeatherIcons.AlertCircle
                MessageSnackbarType.INFO -> FeatherIcons.AlertCircle
            }
            Icon(
                imageVector = icon,
                contentDescription = null, // Decorative
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = snackbarData.visuals.message,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium
            )
          
        }
    }
}