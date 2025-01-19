package dev.alephany.fontpad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A row of keyboard keys that can handle both regular and special keys.
 *
 * @param keys List of key labels to display
 * @param modifier Modifier for the row's layout
 * @param keyWeights Map of key labels to their relative weights in the row
 * @param specialKeys Set of keys that should be treated as special
 * @param activeKeys Set of keys that should be shown in active state
 * @param onKeyClick Callback for when a regular key is clicked
 * @param onSpecialKeyClick Callback for when a special key is clicked
 */
@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyWeights: Map<String, Float> = emptyMap(),
    specialKeys: Set<String> = emptySet(),
    activeKeys: Set<String> = emptySet(),
    onSpecialKeyClick: (String) -> Unit = {},
    onSpecialKeyPress: (String) -> Unit = {},
    onSpecialKeyRelease: (String) -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        keys.forEach { key ->
            val weight = keyWeights[key] ?: 1f
            val isSpecial = key in specialKeys
            val isActive = key in activeKeys

            if (isSpecial) {
                KeyboardKey(
                    text = key,
                    modifier = Modifier.weight(weight),
                    isSpecialKey = true,
                    isActive = isActive,
                    onPress = { onSpecialKeyPress(key) },
                    onRelease = { onSpecialKeyRelease(key) },
                    onClick = { onSpecialKeyClick(key) }
                )
            } else {
                KeyboardKey(
                    text = key,
                    modifier = Modifier.weight(weight),
                    onClick = { onKeyClick(key) }
                )
            }
        }
    }
}