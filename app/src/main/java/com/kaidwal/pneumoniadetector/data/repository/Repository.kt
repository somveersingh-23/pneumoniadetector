package com.kaidwal.pneumoniadetector.data.repository

import com.kaidwal.pneumoniadetector.data.api.RetrofitClient
import com.kaidwal.pneumoniadetector.data.model.ErrorResponse
import com.kaidwal.pneumoniadetector.data.model.HealthResponse
import com.kaidwal.pneumoniadetector.data.model.PredictionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: ErrorResponse) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class PneumoniaRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun checkHealth(): Result<HealthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.checkHealth()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else if (response.code() == 502) {
                // ✅ Special handling for cold start
                Result.Error(ErrorResponse(
                    "Server is waking up. This may take up to 60 seconds. Please wait...",
                    502
                ))
            } else {
                Result.Error(ErrorResponse("Health check failed", response.code()))
            }
        } catch (e: java.net.SocketTimeoutException) {
            Result.Error(ErrorResponse(
                "Server is starting up. Please try again in 30 seconds.",
                0
            ))
        } catch (e: Exception) {
            Result.Error(ErrorResponse(e.message ?: "Network error occurred"))
        }
    }


    suspend fun predictPneumonia(imageFile: File): Result<PredictionResponse> =
        withContext(Dispatchers.IO) {
            try {
                val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestBody
                )

                val response = apiService.predictPneumonia(multipartBody)

                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Prediction failed"
                    Result.Error(ErrorResponse(errorMsg, response.code()))
                }
            } catch (e: Exception) {
                Result.Error(ErrorResponse(e.message ?: "Network error occurred"))
            }
        }
}
