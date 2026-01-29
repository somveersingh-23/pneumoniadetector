package com.kaidwal.pneumoniadetector.data.model

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("diagnosis") val diagnosis: String,
    @SerializedName("confidence") val confidence: Double,
    @SerializedName("risk_level") val riskLevel: String,
    @SerializedName("probability_scores") val probabilityScores: ProbabilityScores,
    @SerializedName("recommendations") val recommendations: List<String>,
    @SerializedName("model_info") val modelInfo: ModelInfo,
    @SerializedName("disclaimer") val disclaimer: String
)

data class ProbabilityScores(
    @SerializedName("NORMAL") val normal: Double,
    @SerializedName("PNEUMONIA") val pneumonia: Double
)

data class ModelInfo(
    @SerializedName("model_name") val modelName: String,
    @SerializedName("recall") val recall: String,
    @SerializedName("false_negative_rate") val falseNegativeRate: String,
    @SerializedName("note") val note: String
)

data class HealthResponse(
    @SerializedName("status") val status: String,
    @SerializedName("model_loaded") val modelLoaded: Boolean,
    @SerializedName("version") val version: String,
    @SerializedName("timestamp") val timestamp: String
)

data class ErrorResponse(
    val message: String,
    val code: Int = 0
)
