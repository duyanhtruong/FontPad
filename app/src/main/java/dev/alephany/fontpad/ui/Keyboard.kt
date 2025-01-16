package dev.alephany.fontpad.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alephany.fontpad.state.ClipboardItem
import dev.alephany.fontpad.state.KeyboardLayout
import dev.alephany.fontpad.state.ShiftState
import dev.alephany.fontpad.ui.keyboard.AlphabeticKeyboard
import dev.alephany.fontpad.ui.keyboard.SymbolKeyboard1
import dev.alephany.fontpad.ui.keyboard.SymbolKeyboard2

/**
 * Main keyboard composable that acts as the entry point for the IME UI.
 * Handles layout switching between different keyboard views and clipboard.
 *
 * @param currentLayout The current keyboard layout to display
 * @param shiftState The current state of the shift key
 * @param clipboardItems Current clipboard history items
 * @param onKeyClick Callback for when a regular key is clicked
 * @param onAction Callback for when a special action is triggered
 * @param modifier Optional modifier for the keyboard
 */
@Composable
fun Keyboard(
    currentLayout: KeyboardLayout,
    shiftState: ShiftState,
    clipboardItems: List<ClipboardItem>,
    onKeyClick: (String) -> Unit,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            when (currentLayout) {
                KeyboardLayout.CLIPBOARD -> {
                    ClipboardView(
                        items = clipboardItems,
                        onPaste = { text ->
                            onAction(KeyboardAction.PasteFromClipboard(text))
                        },
                        onDelete = { text ->
                            onAction(KeyboardAction.DeleteFromClipboard(text))
                        },
                        onClear = {
                            onAction(KeyboardAction.ClearClipboard)
                        },
                        onDismiss = {
                            onAction(KeyboardAction.HideClipboard)
                        }
                    )
                }

                KeyboardLayout.ALPHABETIC -> {
                    AlphabeticKeyboard(
                        shiftState = shiftState,
                        onKeyClick = onKeyClick,
                        onAction = onAction
                    )
                }

                KeyboardLayout.SYMBOL_1 -> {
                    SymbolKeyboard1(
                        onKeyClick = onKeyClick,
                        onAction = onAction
                    )
                }

                KeyboardLayout.SYMBOL_2 -> {
                    SymbolKeyboard2(
                        onKeyClick = onKeyClick,
                        onAction = onAction
                    )
                }
            }
        }
    }
}