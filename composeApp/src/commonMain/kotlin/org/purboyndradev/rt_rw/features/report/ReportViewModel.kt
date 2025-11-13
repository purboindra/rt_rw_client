package org.purboyndradev.rt_rw.features.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ReportViewModel(
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


            _uiState.update { it.copy(isSubmitting = false) }
        }
    }

}