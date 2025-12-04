package org.purboyndradev.rt_rw.features.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.purboyndradev.rt_rw.core.data.remote.mapper.toRes
import org.purboyndradev.rt_rw.core.data.remote.params.CreateReportParams
import org.purboyndradev.rt_rw.core.domain.Result
import org.purboyndradev.rt_rw.domain.usecases.CreateReportUseCase
import org.purboyndradev.rt_rw.domain.usecases.FetchAllReportsUseCase

class ReportViewModel(
    private val createReportUseCase: CreateReportUseCase,
    private val fetchAllReportsUseCase: FetchAllReportsUseCase
) : ViewModel() {

    private val _reportsState = MutableStateFlow(ReportState())
    val reportsState = _reportsState.asStateFlow()

    private val _resultImagePickerLauncher = MutableStateFlow<PhotoResult?>(null)
    val resultImagePickerLauncher = _resultImagePickerLauncher.asStateFlow()

    private val _createReportState = MutableStateFlow(CreateReportState())
    val createReportState = _createReportState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _createReportState.update {
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isNotBlank()) null else it.titleError
            )
        }
    }

    fun onClearSuccessState() {
        _createReportState.update {
            it.copy(
                success = null
            )
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _createReportState.update {
            it.copy(
                description = newDescription,
                descriptionError = if (newDescription.isNotBlank()) null else it.descriptionError
            )
        }
    }

    fun onImageCaptured(bytes: ByteArray?) {
        _createReportState.update {
            it.copy(capturedImageBytes = bytes)
        }
    }

    fun submitReport() {
        val title = _createReportState.value.title
        val description = _createReportState.value.description
        val image = _createReportState.value.capturedImageBytes

        val hasError = title.isBlank() || description.isBlank()

        _createReportState.update {
            it.copy(
                titleError = if (title.isBlank()) "Judul tidak boleh kosong" else null,
                descriptionError = if (description.isBlank()) "Deskripsi tidak boleh kosong" else null
            )
        }

        if (hasError) {
            return
        }

        viewModelScope.launch {
            _createReportState.update { it.copy(isSubmitting = true) }
            val params = CreateReportParams(
                title,
                description,
                image!!,
                fileName = "image.jpg"
            )
            val result = createReportUseCase.invoke(
                params
            )

            when (result) {
                is Result.Success -> {
                    _createReportState.update {
                        it.copy(
                            success = "Laporan berhasil dikirim"
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error.toRes()
                    _createReportState.update {
                        it.copy(
                            error = error
                        )
                    }
                }
            }
            _createReportState.update { it.copy(isSubmitting = false) }
        }
    }

    fun fetchAllReports() {
        viewModelScope.launch {
            _reportsState.update {
                it.copy(
                    isLoading = true
                )
            }

            when (val result = fetchAllReportsUseCase.invoke()) {
                is Result.Success -> {
                    val reports = result.data
                    _reportsState.update {
                        it.copy(
                            reports = reports
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error.toRes()
                    _reportsState.update {
                        it.copy(
                            error = error
                        )
                    }
                }
            }

            _reportsState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }

}