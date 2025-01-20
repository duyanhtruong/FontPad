package dev.alephany.fontpad.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.alephany.fontpad.state.KeyboardTheme
import dev.alephany.fontpad.state.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    viewModel: ThemeSettingsViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val themeState by viewModel.themeState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // App Theme Section
            Text(
                text = "App Theme",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("System default") },
                trailingContent = {
                    RadioButton(
                        selected = themeState.mode == ThemeMode.SYSTEM,
                        onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) }
                    )
                }
            )
            ListItem(
                headlineContent = { Text("Light") },
                trailingContent = {
                    RadioButton(
                        selected = themeState.mode == ThemeMode.LIGHT,
                        onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) }
                    )
                }
            )
            ListItem(
                headlineContent = { Text("Dark") },
                trailingContent = {
                    RadioButton(
                        selected = themeState.mode == ThemeMode.DARK,
                        onClick = { viewModel.setThemeMode(ThemeMode.DARK) }
                    )
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // Keyboard Theme Section
            Text(
                text = "Keyboard Theme",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Default") },
                supportingContent = { Text("Light theme") },
                trailingContent = {
                    RadioButton(
                        selected = themeState.keyboardTheme == KeyboardTheme.DEFAULT,
                        onClick = { viewModel.setKeyboardTheme(KeyboardTheme.DEFAULT) }
                    )
                }
            )
            ListItem(
                headlineContent = { Text("Dark") },
                trailingContent = {
                    RadioButton(
                        selected = themeState.keyboardTheme == KeyboardTheme.DARK,
                        onClick = { viewModel.setKeyboardTheme(KeyboardTheme.DARK) }
                    )
                }
            )
        }
    }
}