package dev.alephany.fontpad.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.alephany.fontpad.data.ThemeDataStore
import dev.alephany.fontpad.font.FontManager
import dev.alephany.fontpad.ui.settings.FontSettingsScreen
import dev.alephany.fontpad.ui.settings.SettingsScreen
import dev.alephany.fontpad.ui.settings.ThemeSettingsScreen
import dev.alephany.fontpad.ui.settings.FontSettingsViewModel
import dev.alephany.fontpad.ui.settings.ThemeSettingsViewModel

sealed class Screen(val route: String) {
    object Settings : Screen("settings")
    object FontSettings : Screen("font_settings")
    object ThemeSettings : Screen("theme_settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Create managers and view models
    val fontManager = remember { FontManager(context) }
    val themeDataStore = remember { ThemeDataStore(context) }

    val fontSettingsViewModel = remember { FontSettingsViewModel(fontManager) }
    val themeSettingsViewModel = remember { ThemeSettingsViewModel(themeDataStore) }

    NavHost(
        navController = navController,
        startDestination = Screen.Settings.route
    ) {
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToFontSettings = {
                    navController.navigate(Screen.FontSettings.route)
                },
                onNavigateToThemeSettings = {
                    navController.navigate(Screen.ThemeSettings.route)
                }
            )
        }

        composable(Screen.FontSettings.route) {
            FontSettingsScreen(
                viewModel = fontSettingsViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(Screen.ThemeSettings.route) {
            ThemeSettingsScreen(
                viewModel = themeSettingsViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}