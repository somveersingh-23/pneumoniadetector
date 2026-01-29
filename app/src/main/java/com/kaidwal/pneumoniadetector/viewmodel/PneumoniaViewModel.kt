package com.kaidwal.pneumoniadetector.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaidwal.pneumoniadetector.data.model.PredictionResponse
import com.kaidwal.pneumoniadetector.data.repository.PneumoniaRepository
import com.kaidwal.pneumoniadetector.data.repository.Result
import kotlinx.coroutines.launch
import java.io.File

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val response: PredictionResponse) : UiState()
    data class Error(val message: String) : UiState()
}

class PneumoniaViewModel : ViewModel() {

    private val repository = PneumoniaRepository()

    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState: State<UiState> = _uiState

    private val _isServerHealthy = mutableStateOf(false)
    val isServerHealthy: State<Boolean> = _isServerHealthy

    private val _selectedImageUri = mutableStateOf<android.net.Uri?>(null)
    val selectedImageUri: State<android.net.Uri?> = _selectedImageUri

    init {
        checkServerHealth()
    }

    fun checkServerHealth() {
        viewModelScope.launch {
            when (val result = repository.checkHealth()) {
                is Result.Success -> {
                    _isServerHealthy.value = result.data.modelLoaded
                }
                is Result.Error -> {
                    _isServerHealthy.value = false
                }
                else -> {}
            }
        }
    }

    fun setSelectedImage(uri: android.net.Uri?) {
        _selectedImageUri.value = uri
    }

    fun predictPneumonia(imageFile: File) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            when (val result = repository.predictPneumonia(imageFile)) {
                is Result.Success -> {
                    _uiState.value = UiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(
                        result.error.message
                    )
                }
                else -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
        _selectedImageUri.value = null
    }
}
