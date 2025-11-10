package org.purboyndradev.rt_rw.features.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.FetchAllNewsUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchNewsByIdUseCase

class NewsViewModel(
    private val fetchAllNewsUseCase: FetchAllNewsUseCase,
    private val fetchNewsByIdUseCase: FetchNewsByIdUseCase,
) : ViewModel() {

    private val _newsState: MutableStateFlow<NewsState> = MutableStateFlow(
        NewsState(
        )
    )
    val newsState = _newsState.asStateFlow()

    fun fetchAllNews() {
        viewModelScope.launch {
            _newsState.value = _newsState.value.copy(
                isLoading = true
            )

            val result = fetchAllNewsUseCase.invoke()
            when (result) {
                is Result.Error -> {
                    val error = result.error.toRes()
                    _newsState.value = _newsState.value.copy(error = error)
                }

                is Result.Success -> {
                    val news = result.data
                    _newsState.value = _newsState.value.copy(
                        news = news
                    )
                }
            }

            _newsState.value = _newsState.value.copy(
                isLoading = false
            )
        }
    }

    fun fetchNewsById(id: String) {
        viewModelScope.launch {
            _newsState.value = _newsState.value.copy(
                isLoading = true
            )

            val result = fetchNewsByIdUseCase.invoke(id)

            when (result) {
                is Result.Error -> {
                    val error = result.error.toRes()
                    _newsState.value = _newsState.value.copy(
                        error = error
                    )
                }

                is Result.Success -> {
                    val news = result.data
                    _newsState.value = _newsState.value.copy(
                        detailNews = news
                    )
                }
            }

            _newsState.value = _newsState.value.copy(
                isLoading = false
            )

        }
    }

}