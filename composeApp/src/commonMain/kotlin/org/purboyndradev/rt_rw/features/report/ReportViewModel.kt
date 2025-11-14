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


class ReportViewModel(
    private val createReportUseCase: CreateReportUseCase
) : ViewModel() {

    private val _resultImagePickerLauncher = MutableStateFlow<PhotoResult?>(null)
    val resultImagePickerLauncher = _resultImagePickerLauncher.asStateFlow()

    private val _uiState = MutableStateFlow(CreateReportState())
    val uiState = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.update {
            it.copy(
                title = newTitle,
                titleError = if (newTitle.isNotBlank()) null else it.titleError
            )
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update {
            it.copy(
                description = newDescription,
                descriptionError = if (newDescription.isNotBlank()) null else it.descriptionError
            )
        }
    }

    fun onImageCaptured(bytes: ByteArray?) {
        _uiState.update {
            it.copy(capturedImageBytes = bytes)
        }
    }

    fun submitReport() {
        val title = _uiState.value.title
        val description = _uiState.value.description
        val image = _uiState.value.capturedImageBytes

        val hasError = title.isBlank() || description.isBlank()

        _uiState.update {
            it.copy(
                titleError = if (title.isBlank()) "Judul tidak boleh kosong" else null,
                descriptionError = if (description.isBlank()) "Deskripsi tidak boleh kosong" else null
            )
        }

        if (hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
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
                    _uiState.update {
                        it.copy(
                            success = "Laporan berhasil dikirim"
                        )
                    }
                }

                is Result.Error -> {
                    val error = result.error.toRes()
                    _uiState.update {
                        it.copy(
                            error = error
                        )
                    }
                }
            }

            _uiState.update { it.copy(isSubmitting = false) }
        }
    }

}