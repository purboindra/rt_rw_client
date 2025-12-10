package org.purboyndradev.rt_rw.features.report.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.Plus
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.core.data.remote.params.QueryParams
import org.purboyndradev.rt_rw.features.components.EmptyStateView
import org.purboyndradev.rt_rw.features.components.ErrorViewCompose
import org.purboyndradev.rt_rw.features.components.ReportContent
import org.purboyndradev.rt_rw.features.navigation.CreateReport
import org.purboyndradev.rt_rw.features.navigation.ReportDetail

@Composable
fun ReportsScreen(
    navHostController: NavHostController, modifier: Modifier = Modifier,
) {

    val reportViewModel = koinViewModel<ReportViewModel>()

    val reportsState by reportViewModel.reportsState.collectAsStateWithLifecycle()
    val searchQuery by reportViewModel.searchQuery.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        reportViewModel.fetchAllReports()
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = modifier.fillMaxWidth().padding(
                horizontal = 18.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Reports", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {
                navHostController.navigate(CreateReport)
            }) {
                Icon(
                    FeatherIcons.Plus, contentDescription = "Add",
                    tint = Color.Black
                )
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                reportViewModel.onQueryChange(it)
            },
            modifier = modifier.fillMaxWidth().padding(
                horizontal = 18.dp
            ),
            placeholder = {
                Text("Search")
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                    val queryParams = QueryParams(
                        query = searchQuery
                    )

                    reportViewModel.fetchAllReports(queryParams)

                    keyboardController?.hide()
                }
            )
        )

        when {
            reportsState.isLoading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            reportsState.error != null -> {
                ErrorViewCompose(
                    modifier,
                    reportsState.error ?: "Unknown Error"
                )
            }

            reportsState.reports.isNotEmpty() -> {
                LazyColumn(
                    modifier = modifier.fillMaxSize().padding(
                        horizontal = 12.dp,
                    )
                ) {
                    items(reportsState.reports) { report ->
                        ReportContent(
                            report,
                            onReportTapped = { id ->
                                navHostController.navigate(ReportDetail(id))
                            }
                        )
                    }
                }
            }

            else -> {
                EmptyStateView(modifier, "Tidak ada laporan")
            }
        }
    }
}