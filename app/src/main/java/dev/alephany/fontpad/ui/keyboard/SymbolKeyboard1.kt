package dev.alephany.fontpad.ui.keyboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alephany.fontpad.data.KeyboardMappings
import dev.alephany.fontpad.ui.KeyboardAction
import dev.alephany.fontpad.ui.KeyboardRow

/**
 * First symbols keyboard layout (@#$, etc.).
 * Primary symbol layout with common symbols and numbers.
 */
@Composable
internal fun SymbolKeyboard1(
    onKeyClick: (String) -> Unit,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Clipboard bezel
        KeyboardBezel(
            onClipboardClick = { onAction(KeyboardAction.ShowClipboard) },
            onFontSelectorClick = { onAction(KeyboardAction.ShowFontSelector) }
        )

        // Number row (reused from alphabetic)
        KeyboardRow(
            keys = KeyboardMappings.numberRow,
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // First symbol row
        KeyboardRow(
            keys = KeyboardMappings.Symbols.topSymbolRow1,
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Second symbol row
        KeyboardRow(
            keys = KeyboardMappings.Symbols.middleSymbolRow1,
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Bottom row with actions
        KeyboardRow(
            keys = KeyboardMappings.Symbols.actionSymbolRow1,
            keyWeights = KeyboardMappings.Symbols.keyWeights,
            specialKeys = setOf("ABC", "=\\<", "space", "enter", "⌫"),
            onKeyClick = onKeyClick,
            onSpecialKeyClick = { key ->
                when (key) {
                    "ABC" -> onAction(KeyboardAction.SwitchToAlphabetic)
                    "=\\<" -> onAction(KeyboardAction.SwitchToSymbols2)
                    "space" -> onAction(KeyboardAction.Space)
                    "⌫" -> onAction(KeyboardAction.Backspace)
                    "enter" -> onAction(KeyboardAction.Enter)
                }
            }
        )
    }
}