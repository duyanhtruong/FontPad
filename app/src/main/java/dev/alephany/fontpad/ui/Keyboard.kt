package dev.alephany.fontpad.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.alephany.fontpad.state.ClipboardItem
import dev.alephany.fontpad.state.FontData
import dev.alephany.fontpad.state.KeyboardLayout
import dev.alephany.fontpad.state.KeyboardTheme
import dev.alephany.fontpad.state.ShiftState
import dev.alephany.fontpad.ui.keyboard.AlphabeticKeyboard
import dev.alephany.fontpad.ui.keyboard.ClipboardView
import dev.alephany.fontpad.ui.keyboard.FontSelector
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
    fonts: List<FontData>,
    selectedFontId: String?,
    keyboardTheme: KeyboardTheme,
    onKeyClick: (String) -> Unit,
    onAction: (KeyboardAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = when (keyboardTheme) {
        KeyboardTheme.DEFAULT -> lightColorScheme()
        KeyboardTheme.DARK -> darkColorScheme()
    }

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
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
                    KeyboardLayout.FONT_SELECTOR -> {
                        FontSelector(
                            fonts = fonts,
                            selectedFontId = selectedFontId,
                            onFontSelected = { fontId ->
                                onAction(KeyboardAction.SelectFont(fontId))
                            },
                            onDismiss = { onAction(KeyboardAction.HideFontSelector) }
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
}