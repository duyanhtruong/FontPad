package dev.alephany.fontpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alephany.fontpad.clipboard.ClipboardManager
import dev.alephany.fontpad.font.FontManager
import dev.alephany.fontpad.state.ClipboardItem
import dev.alephany.fontpad.state.FontData
import dev.alephany.fontpad.state.KeyboardLayout
import dev.alephany.fontpad.state.KeyboardState
import dev.alephany.fontpad.state.ShiftState
import dev.alephany.fontpad.ui.KeyboardAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing keyboard state and handling keyboard actions.
 */
class KeyboardViewModel(
    private val clipboardManager: ClipboardManager,
    private val fontManager: FontManager
) : ViewModel() {

    private val _keyboardState = MutableStateFlow(KeyboardState())
    val keyboardState: StateFlow<KeyboardState> = _keyboardState.asStateFlow()

    // Expose clipboard history as StateFlow
    val clipboardHistory = clipboardManager.history

    // Expose fonts as StateFlow with hot sharing
    private val _fonts = MutableStateFlow<List<FontData>>(emptyList())
    val fonts: StateFlow<List<FontData>> = _fonts.asStateFlow()

    init {
        // Collect font updates in viewModelScope
        viewModelScope.launch {
            fontManager.fonts.collect { updatedFonts ->
                _fonts.value = updatedFonts
            }
        }
    }

    fun handleAction(action: KeyboardAction) {
        when (action) {
            KeyboardAction.Shift -> handleShiftPress()
            KeyboardAction.SwitchToAlphabetic -> switchLayout(KeyboardLayout.ALPHABETIC)
            KeyboardAction.SwitchToSymbols -> switchLayout(KeyboardLayout.SYMBOL_1)
            KeyboardAction.SwitchToSymbols2 -> switchLayout(KeyboardLayout.SYMBOL_2)
            KeyboardAction.ShowClipboard -> {
                _keyboardState.update { currentState ->
                    currentState.copy(
                        previousLayout = currentState.currentLayout,
                        currentLayout = KeyboardLayout.CLIPBOARD
                    )
                }
            }
            KeyboardAction.HideClipboard -> {
                _keyboardState.update { currentState ->
                    currentState.copy(
                        currentLayout = currentState.previousLayout
                    )
                }
            }
            KeyboardAction.ClearClipboard -> clipboardManager.clearHistory()
            is KeyboardAction.PasteFromClipboard -> {
                // After pasting, return to previous layout
                _keyboardState.update { currentState ->
                    currentState.copy(
                        currentLayout = currentState.previousLayout
                    )
                }
            }
            is KeyboardAction.DeleteFromClipboard -> {
                clipboardManager.deleteFromHistory(action.content)
            }

            KeyboardAction.ShowFontSelector -> {
                _keyboardState.update { currentState ->
                    currentState.copy(
                        previousLayout = currentState.currentLayout,
                        currentLayout = KeyboardLayout.FONT_SELECTOR
                    )
                }
            }

            KeyboardAction.HideFontSelector -> {
                _keyboardState.update { currentState ->
                    currentState.copy(
                        currentLayout = currentState.previousLayout
                    )
                }
            }

            is KeyboardAction.SelectFont -> {
                _keyboardState.update { currentState ->
                    currentState.copy(
                        selectedFontId = action.fontId,
                        currentLayout = currentState.previousLayout
                    )
                }
            }

            else -> { /* Other actions are handled by IME service */ }
        }
    }

    private fun handleShiftPress() {
        val currentTime = System.currentTimeMillis()
        _keyboardState.update { currentState ->
            when {
                // If already in CAPS_LOCK, turn it off
                currentState.shiftState == ShiftState.CAPS_LOCK -> {
                    currentState.copy(
                        shiftState = ShiftState.OFF,
                        lastShiftPressTime = currentTime
                    )
                }
                // If in SHIFTED state and not a double-tap, turn it off
                currentState.shiftState == ShiftState.SHIFTED &&
                        (currentTime - currentState.lastShiftPressTime >= 300) -> {
                    currentState.copy(
                        shiftState = ShiftState.OFF,
                        lastShiftPressTime = currentTime
                    )
                }
                // Double tap detection (within 300ms)
                currentTime - currentState.lastShiftPressTime < 300 &&
                        currentState.shiftState == ShiftState.SHIFTED -> {
                    currentState.copy(
                        shiftState = ShiftState.CAPS_LOCK,
                        lastShiftPressTime = currentTime
                    )
                }
                // Single tap or any other state: just SHIFTED
                else -> {
                    currentState.copy(
                        shiftState = ShiftState.SHIFTED,
                        lastShiftPressTime = currentTime
                    )
                }
            }
        }
    }

    /**
     * Consumes shift state after a letter is typed.
     * Only resets if in SHIFTED mode, not CAPS_LOCK.
     */
    fun consumeShift() {
        _keyboardState.update { currentState ->
            if (currentState.shiftState == ShiftState.SHIFTED) {
                currentState.copy(shiftState = ShiftState.OFF)
            } else {
                currentState
            }
        }
    }

    private fun switchLayout(layout: KeyboardLayout) {
        _keyboardState.update { it.copy(currentLayout = layout) }
    }

    fun refreshFonts() {
        viewModelScope.launch {
            // This will trigger the FontManager to reload fonts and emit a new value
            fontManager.loadFonts()
        }
    }

    fun resetState() {
        _keyboardState.value = KeyboardState()
    }
}