package dev.alephany.fontpad.ui

/**
 * Defines all possible keyboard actions that can be triggered.
 * This acts as a contract between the keyboard UI and the IME service.
 */
sealed interface KeyboardAction {
    // Standard keyboard actions
    data object Shift : KeyboardAction
    data object Backspace : KeyboardAction
    data object Tab : KeyboardAction
    data object Space : KeyboardAction
    data object Enter : KeyboardAction

    // Layout switching actions
    data object SwitchToSymbols : KeyboardAction
    data object SwitchToSymbols2 : KeyboardAction
    data object SwitchToAlphabetic : KeyboardAction

    // Clipboard-related actions
    data object ShowClipboard : KeyboardAction
    data object HideClipboard : KeyboardAction
    data object ClearClipboard : KeyboardAction
    data class PasteFromClipboard(val text: String) : KeyboardAction
    data class DeleteFromClipboard(val content: String) : KeyboardAction
}