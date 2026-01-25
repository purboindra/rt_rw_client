package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable

fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String? = null,
    email: String? = null,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                HeaderProfile(
                    modifier = Modifier,
                    userName = userName ?: "",
                    email = email,
                    phoneNumber = ""
                )
            }

            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Bantuan & Informasi",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                CardInformation(
                    title = "Informasi Akun"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CardInformation(
                    title = "Kebijakan Privasi"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CardInformation(
                    title = "FAQ"
                )
            }
        }
    }
}