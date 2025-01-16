package dev.alephany.fontpad.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
internal fun KeyboardRow(
    keys: List<String>,
    modifier: Modifier = Modifier,
    keyWeights: Map<String, Float> = emptyMap(),
    specialKeys: Set<String> = emptySet(),
    activeKeys: Set<String> = emptySet(),
    onKeyClick: (String) -> Unit = {},
    onSpecialKeyClick: (String) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        keys.forEach { key ->
            KeyboardKey(
                text = key,
                modifier = Modifier.weight(keyWeights[key] ?: 1f),
                isSpecialKey = key in specialKeys,
                isActive = key in activeKeys,
                onClick = {
                    if (key in specialKeys) {
                        onSpecialKeyClick(key)
                    } else {
                        onKeyClick(key)
                    }
                }
            )
        }
    }
}