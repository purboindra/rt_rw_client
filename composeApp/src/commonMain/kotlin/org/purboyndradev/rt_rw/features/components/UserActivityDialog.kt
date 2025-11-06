package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = if (usersActivityState.isLoading) "Loading..." else "This is a dialog")
        }
    }
}