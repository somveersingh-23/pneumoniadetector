package com.kaidwal.pneumoniadetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.kaidwal.pneumoniadetector.ui.navigation.NavigationGraph
import com.kaidwal.pneumoniadetector.ui.theme.PneumoniaDetectorTheme
import com.kaidwal.pneumoniadetector.viewmodel.PneumoniaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ✅ ADD THIS - Catch hover exit crash (Compose bug workaround)
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            if (throwable is IllegalStateException &&
                throwable.message?.contains("ACTION_HOVER_EXIT") == true) {
                android.util.Log.w("MainActivity", "Caught hover exit bug - ignoring", throwable)
                // Don't crash, just log it
                return@setDefaultUncaughtExceptionHandler
            } else {
                // Re-throw other exceptions
                val oldHandler = Thread.getDefaultUncaughtExceptionHandler()
                oldHandler?.uncaughtException(thread, throwable)
            }
        }
        setContent {
            PneumoniaDetectorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: PneumoniaViewModel = viewModel()

                    NavigationGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
