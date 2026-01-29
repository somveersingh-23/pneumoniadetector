package com.kaidwal.pneumoniadetector.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kaidwal.pneumoniadetector.ui.screens.HomeScreen
import com.kaidwal.pneumoniadetector.ui.screens.ResultScreen
import com.kaidwal.pneumoniadetector.ui.screens.ScanScreen
import com.kaidwal.pneumoniadetector.viewmodel.PneumoniaViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Scan : Screen("scan")
    object Result : Screen("result")
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: PneumoniaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
