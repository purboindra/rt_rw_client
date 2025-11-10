package org.purboyndradev.rt_rw.features.news.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import org.koin.compose.viewmodel.koinViewModel
import org.purboyndradev.rt_rw.features.components.ErrorCompose
import org.purboyndradev.rt_rw.features.components.NewsDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    id: String,
    navHostController: NavHostController
) {

    val newsViewModel = koinViewModel<NewsViewModel>()
    val newsState by newsViewModel.newsState.collectAsStateWithLifecycle()

    val isLoading = newsState.isLoading
    val error = newsState.error
    val news = newsState.detailNews

    LaunchedEffect(Unit) {
        newsViewModel.fetchNewsById(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("News")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        },
                    ) {
                        Icon(imageVector = FeatherIcons.ArrowLeft, contentDescription = null)
                    }
                }

            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).padding(
                horizontal = 16.dp
            )
        ) {
            item {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    error != null -> {
                        ErrorCompose(
                            modifier = Modifier.fillParentMaxSize(),
                            message = error
                        )
                    }

                    news != null -> {
                        NewsDetailContent(
                            news,
                        )
                    }

                    else -> {
                        Box(modifier = Modifier.fillParentMaxSize()) {
                            Text("No Detail News Available")
                        }
                    }

                }
            }
        }
    }
}