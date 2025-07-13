package org.purboyndradev.rt_rw.features.activity.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCompose(navHostController: NavHostController, title: String) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            Icon(
                FeatherIcons.ArrowLeft, contentDescription = null,
                modifier = Modifier.padding(horizontal = 18.dp)
                    .clickable {
                        navHostController.popBackStack()
                    }
            )
        }
    )
}