package org.purboyndradev.rt_rw.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.Info
import compose.icons.feathericons.Plus
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.navigation.Login


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    userName: String,
) {
    val profileViewModel = koinViewModel<ProfileViewModel>()
    val logOutState by profileViewModel.logOutState.collectAsStateWithLifecycle()

    LaunchedEffect(logOutState.success) {
        if (logOutState.success) {
            navHostController.navigate(Login)
        }
    }

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
                HeaderProfile(modifier = Modifier)
            }

            Column  {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Bantuan & Informasi", style = MaterialTheme.typography.titleMedium.copy(
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