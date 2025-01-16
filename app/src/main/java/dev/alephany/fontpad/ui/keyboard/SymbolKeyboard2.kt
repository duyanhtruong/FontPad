package dev.alephany.fontpad.ui.keyboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alephany.fontpad.data.KeyboardMappings
import dev.alephany.fontpad.ui.KeyboardAction
import dev.alephany.fontpad.ui.KeyboardRow

/**
 * Second symbols keyboard layout (π√∆, etc.).
 * Extended symbol layout with mathematical and special symbols.
 */
@Composable
internal fun SymbolKeyboard2(
    onKeyClick: (String) -> Unit,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Clipboard bezel
        KeyboardBezel(
            onClipboardClick = { onAction(KeyboardAction.ShowClipboard) }
        )

        // First symbol row
        KeyboardRow(
            keys = KeyboardMappings.Symbols.topSymbolRow2,
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Second symbol row
        KeyboardRow(
            keys = KeyboardMappings.Symbols.middleSymbolRow2,
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Third row with backspace
        KeyboardRow(
            keys = KeyboardMappings.Symbols.bottomSymbolRow2,
            keyWeights = KeyboardMappings.Symbols.keyWeights,
            specialKeys = setOf("⌫"),
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key ->
                if (key == "⌫") onAction(KeyboardAction.Backspace)
            }
        )

        // Bottom row with actions
        KeyboardRow(
            keys = KeyboardMappings.Symbols.actionSymbolRow2,
            keyWeights = KeyboardMappings.Symbols.keyWeights,
            specialKeys = setOf("ABC", "?123", "space", "enter"),
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key ->
                when (key) {
                    "ABC" -> onAction(KeyboardAction.SwitchToAlphabetic)
                    "?123" -> onAction(KeyboardAction.SwitchToSymbols)
                    "space" -> onAction(KeyboardAction.Space)
                    "enter" -> onAction(KeyboardAction.Enter)
                }
            }
        )
    }
}