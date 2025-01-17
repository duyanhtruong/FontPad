package dev.alephany.fontpad.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.alephany.fontpad.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToFontSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "FontPad Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(16.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Define settings with their icons
            val settings = listOf(
                SettingItem(
                    title = "Theme",
                    description = "Customize appearance",
                    iconRes = R.drawable.ic_theme
                ),
                SettingItem(
                    title = "Font",
                    description = "Manage keyboard fonts",
                    iconRes = R.drawable.ic_font
                ),
                SettingItem(
                    title = "Clipboard",
                    description = "History and settings",
                    iconRes = R.drawable.ic_clipboard
                ),
                SettingItem(
                    title = "About",
                    description = "App information",
                    iconRes = R.drawable.ic_info
                )
            )

            settings.forEachIndexed { index, item ->
                SettingsItem(
                    settingItem = item,
                    isLastItem = index == settings.lastIndex,
                    onClick = {
                        when (item.title) {
                            "Font" -> onNavigateToFontSettings()
                            // Other navigation handlers can be added here
                        }
                    }
                )
            }
        }
    }
}

data class SettingItem(
    val title: String,
    val description: String,
    val iconRes: Int
)

@Composable
fun SettingsItem(
    settingItem: SettingItem,
    isLastItem: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Icon(
                    painter = painterResource(id = settingItem.iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )

                // Text content
                Column {
                    Text(
                        text = settingItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = settingItem.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (!isLastItem) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}