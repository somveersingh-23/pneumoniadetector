package com.kaidwal.pneumoniadetector.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaidwal.pneumoniadetector.ui.components.*
import com.kaidwal.pneumoniadetector.ui.navigation.Screen
import com.kaidwal.pneumoniadetector.ui.theme.*
import com.kaidwal.pneumoniadetector.viewmodel.PneumoniaViewModel
import com.kaidwal.pneumoniadetector.viewmodel.UiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: PneumoniaViewModel
) {
    val uiState by viewModel.uiState
    val selectedImageUri by viewModel.selectedImageUri

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Analysis Result",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.resetState()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->

        when (val state = uiState) {
            is UiState.Success -> {
                val result = state.response

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    // Result Header with Animation
                    ResultHeader(
                        diagnosis = result.diagnosis,
                        confidence = result.confidence,
                        riskLevel = result.riskLevel
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // X-Ray Image Preview
                    selectedImageUri?.let { uri ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Analyzed X-Ray",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Confidence Cards
                    ConfidenceSection(
                        normalProb = result.probabilityScores.normal,
                        pneumoniaProb = result.probabilityScores.pneumonia
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Model Info
                    ModelInfoSection(
                        modelName = result.modelInfo.modelName,
                        recall = result.modelInfo.recall,
                        note = result.modelInfo.note
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Recommendations
                    RecommendationsSection(
                        recommendations = result.recommendations
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Disclaimer
                    DisclaimerCard(disclaimer = result.disclaimer)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    ActionButtons(navController, viewModel)

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
            is UiState.Error -> {
                ErrorResultScreen(
                    errorMessage = state.message,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results available")
                }
            }
        }
    }
}

@Composable
fun ResultHeader(
    diagnosis: String,
    confidence: Double,
    riskLevel: String
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val infiniteTransition = rememberInfiniteTransition(label = "header")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (diagnosis == "PNEUMONIA") {
                            listOf(ErrorRed, AccentOrange)
                        } else {
                            listOf(SuccessGreen, SecondaryGreenLight)
                        }
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon with Animation
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (diagnosis == "PNEUMONIA")
                            Icons.Default.Warning
                        else
                            Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = diagnosis,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = String.format("Confidence: %.2f%%", confidence),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                RiskLevelBadge(riskLevel = riskLevel)
            }
        }
    }
}

@Composable
fun ConfidenceSection(
    normalProb: Double,
    pneumoniaProb: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Probability Analysis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProbabilityBar(
                label = "Normal",
                probability = normalProb,
                color = SuccessGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProbabilityBar(
                label = "Pneumonia",
                probability = pneumoniaProb,
                color = ErrorRed
            )
        }
    }
}

@Composable
fun ModelInfoSection(
    modelName: String,
    recall: String,
    note: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = InfoBlue.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = InfoBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Model Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ModelDetailRow(label = "Model", value = modelName)
            Spacer(modifier = Modifier.height(8.dp))
            ModelDetailRow(label = "Recall", value = recall)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = note,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
fun ModelDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RecommendationsSection(
    recommendations: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = AccentPurple,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Recommendations",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            recommendations.forEachIndexed { index, recommendation ->
                RecommendationItem(
                    text = recommendation,
                    index = index
                )
            }
        }
    }
}

@Composable
fun DisclaimerCard(disclaimer: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = WarningYellow.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = WarningYellow,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "Important Disclaimer",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = WarningYellow
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = disclaimer,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun ActionButtons(
    navController: NavController,
    viewModel: PneumoniaViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        GradientButton(
            text = "Analyze Another X-Ray",
            onClick = {
                viewModel.resetState()
                navController.navigate(Screen.Scan.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            icon = Icons.Default.Add,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                viewModel.resetState()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PrimaryBlue
            ),
            border = androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back to Home")
        }
    }
}

@Composable
fun ErrorResultScreen(
    errorMessage: String,
    navController: NavController,
    viewModel: PneumoniaViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = ErrorRed,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Analysis Failed",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = ErrorRed
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                text = "Try Again",
                onClick = {
                    viewModel.resetState()
                    navController.navigateUp()
                },
                icon = Icons.Default.Refresh,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
