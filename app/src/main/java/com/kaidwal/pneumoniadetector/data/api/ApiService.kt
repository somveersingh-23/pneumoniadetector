package com.kaidwal.pneumoniadetector.data.api

import com.kaidwal.pneumoniadetector.data.model.HealthResponse
import com.kaidwal.pneumoniadetector.data.model.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface PneumoniaApiService {

    @GET("health")
    suspend fun checkHealth(): Response<HealthResponse>

    @Multipart
    @POST("predict")
    suspend fun predictPneumonia(
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>

    @GET("model-info")
    suspend fun getModelInfo(): Response<Map<String, Any>>
}
