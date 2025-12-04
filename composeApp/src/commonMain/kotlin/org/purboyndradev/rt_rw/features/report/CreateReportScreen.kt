package org.purboyndradev.rt_rw.features.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.components.CameraCompose
import org.purboyndradev.rt_rw.features.navigation.CameraPreviewScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(navHostController: NavHostController) {

    val reportViewModel = koinViewModel<ReportViewModel>()

    val resultImagePickerLauncher by reportViewModel.resultImagePickerLauncher.collectAsStateWithLifecycle()
    val createReportState by reportViewModel.createReportState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(createReportState.error, createReportState.success) {
        if (createReportState.error != null) {
            snackbarHostState.showSnackbar(
                "${createReportState.error}"
            )
        } else if (createReportState.success != null) {
            navHostController.previousBackStackEntry?.savedStateHandle?.set(
                "snackbar_message", createReportState.success
            )
            reportViewModel.onClearSuccessState()
            navHostController.popBackStack()
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Report")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = FeatherIcons.ArrowLeft,
                            contentDescription = null
                        )
                    }
                }

            )
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Box(
                    modifier = Modifier.padding(
                        horizontal = 16.dp
                    )
                ) {
                    FilledIconButton(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        onClick = {
                            reportViewModel.submitReport()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        enabled = createReportState.canSubmit,
                    ) {
                        Text(
                            if (createReportState.isSubmitting) "Mengirim..." else
                                "Kirim"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(
                horizontal = 12.dp
            )
        ) {
            item {
                Text(
                    "Laporkan Kejadian", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = createReportState.title,
                        onValueChange = reportViewModel::onTitleChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Judul Kejadian")
                        },
                        isError = createReportState.titleError != null,
                        supportingText = {
                            createReportState.titleError?.let {
                                Text(it)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = createReportState.description,
                        onValueChange = reportViewModel::onDescriptionChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Deskripsi")
                        },
                        maxLines = 4,
                        minLines = 4,
                        isError = createReportState.descriptionError != null,
                        supportingText = {
                            createReportState.descriptionError?.let {
                                Text(it)
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CameraCompose(
                        modifier = Modifier.fillMaxWidth().height(224.dp),
                        resultImagePickerLauncher = resultImagePickerLauncher,
                        onTap = {
                            navHostController.navigate(CameraPreviewScreen)
                        },
                        navHostController = navHostController,
                        capturedImageBytes = createReportState.capturedImageBytes,
                        onSetCapturedImageBytes = reportViewModel::onImageCaptured
                    )
                }
            }
        }
    }
}