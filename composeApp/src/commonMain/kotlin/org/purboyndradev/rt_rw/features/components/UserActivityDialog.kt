package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.purboyndradev.rt_rw.features.activity.presentation.UsersActivityState

@Composable
fun UserActivityDialog(
    onDismissRequest: () -> Unit,
    usersActivityState: UsersActivityState
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            ) {
                when {
                    usersActivityState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    usersActivityState.error != null -> {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Maaf Terjadi Kesalahan", style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.Red,
                                ))
                                Text("Error terjadi",style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.Gray,
                                ))
                            }
                        }
                    }

                    else -> {
                        LazyColumn {
                            item {
                                Text(
                                    text = "Participants",style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            itemsIndexed(usersActivityState.users) { index, user ->
                                Text(
                                    user.name, style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.Gray,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                if (index < usersActivityState.users.lastIndex) {
                                    HorizontalDivider(
                                        Modifier.padding(vertical = 8.dp),
                                        DividerDefaults.Thickness,
                                        DividerDefaults.color
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
