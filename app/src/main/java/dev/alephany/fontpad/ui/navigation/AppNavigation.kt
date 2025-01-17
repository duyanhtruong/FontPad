package dev.alephany.fontpad.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.alephany.fontpad.ui.settings.FontSettingsScreen
import dev.alephany.fontpad.ui.settings.SettingsScreen
import dev.alephany.fontpad.ui.settings.FontSettingsViewModel

sealed class Screen(val route: String) {
    object Settings : Screen("settings")
    object FontSettings : Screen("font_settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val fontSettingsViewModel = FontSettingsViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Settings.route
    ) {
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToFontSettings = {
                    navController.navigate(Screen.FontSettings.route)
                }
            )
        }

        composable(Screen.FontSettings.route) {
            FontSettingsScreen(
                viewModel = fontSettingsViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}