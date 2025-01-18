package dev.alephany.fontpad.ui.keyboard

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alephany.fontpad.data.KeyboardMappings
import dev.alephany.fontpad.state.ShiftState
import dev.alephany.fontpad.ui.KeyboardAction
import dev.alephany.fontpad.ui.KeyboardRow

/**
 * Main alphabetic keyboard layout (QWERTY).
 * Includes number row, letter rows, and special function keys.
 */
@Composable
internal fun AlphabeticKeyboard(
    shiftState: ShiftState,
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

        // Number row
        KeyboardRow(
            keys = KeyboardMappings.numberRow,
            onKeyClick = onKeyClick,
            // Input handling
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // First letter row (QWERTYUIOP)
        KeyboardRow(
            keys = KeyboardMappings.Alphabetic.topRow.map {
                if (shiftState != ShiftState.OFF) it.uppercase() else it
            },
            onKeyClick = onKeyClick,
            // Input handling
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Second letter row (ASDFGHJKL)
        KeyboardRow(
            keys = KeyboardMappings.Alphabetic.middleRow.map {
                if (shiftState != ShiftState.OFF) it.uppercase() else it
            },
            onKeyClick = onKeyClick,
            // input handling
            onSpecialKeyClick = { key -> onKeyClick(key) }
        )

        // Third row (Shift + ZXCVBNM + Backspace)
        KeyboardRow(
            keys = KeyboardMappings.Alphabetic.bottomRow.map { key ->
                when {
                    key == "shift" || key == "⌫" -> key
                    shiftState != ShiftState.OFF -> key.uppercase()
                    else -> key
                }
            },
            keyWeights = KeyboardMappings.Alphabetic.keyWeights,
            specialKeys = setOf("shift", "⌫"),
            onKeyClick = onKeyClick,
            activeKeys = if (shiftState != ShiftState.OFF) setOf("shift") else emptySet(),
            onSpecialKeyClick = { key ->
                when (key) {
                    "shift" -> onAction(KeyboardAction.Shift)
                    "⌫" -> onAction(KeyboardAction.Backspace)
                }
            }
        )

        // Bottom row (Tab + ?123 + Space + .,)
        KeyboardRow(
            keys = KeyboardMappings.Alphabetic.actionRow,
            keyWeights = KeyboardMappings.Alphabetic.keyWeights,
            specialKeys = setOf("tab", "?123", "space", "enter"),
            onSpecialKeyClick = { key ->
                when (key) {
                    "tab" -> onAction(KeyboardAction.Tab)
                    "?123" -> onAction(KeyboardAction.SwitchToSymbols)
                    "space" -> onAction(KeyboardAction.Space)
                    "enter" -> onAction(KeyboardAction.Enter)
                }
            }
        )
    }
}