package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.User
import org.purboyndradev.rt_rw.core.domain.model.UserModel
import org.purboyndradev.rt_rw.features.report.presentation.SectionCard

@Composable
 fun ParticipantsSection(
    participants: List<UserModel>,
    onSeeAllClick: () -> Unit
) {
    SectionCard(title = "Partisipan") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    FeatherIcons.User,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "${participants.size} partisipan",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            TextButton(onClick = onSeeAllClick) {
                Text("Lihat Semua")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (participants.isEmpty()) {
            Text(
                "Belum ada partisipan",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            participants.take(3).forEach { p ->
                Text(
                    "• ${p.name} (${p.role})",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (participants.size > 3) {
                Text(
                    "+${participants.size - 3} lainnya",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
