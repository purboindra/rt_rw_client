package org.purboyndradev.rt_rw.features.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import io.github.ismoy.imagepickerkmp.domain.config.CameraCaptureConfig
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.config.PermissionAndConfirmationConfig
import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher

@Composable
fun CameraPreviewScreen(navHostController: NavHostController){
    val backStackEntry = navHostController.currentBackStackEntry
    val savedStateHandle = backStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.get<String>("captured_image_uri")?.let { uri ->
            savedStateHandle.remove<String>("captured_image_uri")
        }
    }

    ImagePickerLauncher(
        config = ImagePickerConfig(
            onPhotoCaptured = { result ->
                navHostController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("captured_image_uri", result.loadBytes())
                navHostController.popBackStack()
            },
            directCameraLaunch = true,
            onError = {
                navHostController.popBackStack()
            },
            onDismiss = {
                navHostController.popBackStack()
            },
            cameraCaptureConfig = CameraCaptureConfig(
                includeExif = true,
                permissionAndConfirmationConfig = PermissionAndConfirmationConfig(
                    cancelButtonTextIOS = "Dismiss",
                    onCancelPermissionConfigIOS = {
                        navHostController.popBackStack()
                    }
                )
            ),
        )
    )
}