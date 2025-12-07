package org.purboyndradev.rt_rw.features.report.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
//    onBackClick: () -> Unit,
//    onCallReporterClick: (String) -> Unit,
//    onImageClick: (String) -> Unit = {}
    id: String,
    navHostController: NavHostController
) {

    val reportViewModel = koinViewModel<ReportViewModel>()
    val reportState by reportViewModel.reportDetailState.collectAsStateWithLifecycle()

    val report = reportState.report
    val isLoading = reportState.isLoading
    val error = reportState.error

    LaunchedEffect(Unit) {
        reportViewModel.fetchReportById(id)
    }


    Scaffold(
        topBar = {
//            SmallTopAppBar(
//                title = {
//                    Text(
//                        text = "Detail Laporan",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
        }
    ) { innerPadding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error)
                }
            }

            report != null -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Header + status
                    ReportHeaderSection(report)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Image section
                    report.imageUrl.let { url ->
                        ReportImageSection(
                            imageUrl = url,
                            onClick = {
                                //                                onImageClick(url)
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Description
                    ReportDescriptionSection(report.description)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Reporter info
                    ReporterSection(
                        name = report.user.name,
                        address = report.user.address,
                        phone = report.user.phoneNumber,
                        onCallClick = { phone ->
//                            if (phone != null) onCallReporterClick(phone)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ReportMetadataSection(
                        reportId = report.reportId,
                        createdAt = report.createdAt,
                        resolvedAt = report.resolvedAt
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Report tidak ditemukan")
                }
            }
        }
    }
}
