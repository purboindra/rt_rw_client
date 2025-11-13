package org.purboyndradev.rt_rw.features.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.Image
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.delay

@Composable
fun CameraCompose(
    modifier: Modifier = Modifier, onTap: () -> Unit,
    resultImagePickerLauncher: PhotoResult?,
    navHostController: NavHostController,
    capturedImageBytes: ByteArray?,
    onSetCapturedImageBytes: (ByteArray) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    val backStackEntry = navHostController.currentBackStackEntry
    val savedStateHandle = backStackEntry?.savedStateHandle

    LaunchedEffect(resultImagePickerLauncher) {
        if (resultImagePickerLauncher != null) {
            isLoading = true
            delay(300)
            isLoading = false
        }
    }

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.get<ByteArray>("captured_image_uri")?.let { bytes ->
            onSetCapturedImageBytes(bytes)
            savedStateHandle.remove<String>("captured_image_uri")
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(12.dp)
        ).clickable {
            onTap()
        }
    ) {
        when {

            capturedImageBytes != null -> {
                AsyncImage(
                    model = capturedImageBytes,
                    contentDescription = "Image loaded from URI",
                    modifier = Modifier.fillMaxWidth(),
                    onLoading = {
                        isLoading = true
                    },
                    onSuccess = {
                        isLoading = false
                    },
                )
            }

            isLoading -> {
                CircularProgressIndicator()
            }

            else -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = FeatherIcons.Image,
                        contentDescription = "Pick image",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Tap to select image",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        }
    }
}