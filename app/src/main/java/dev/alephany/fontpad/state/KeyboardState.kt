package dev.alephany.fontpad.state

/**
 * Represents the different states of the keyboard layout
 */
enum class KeyboardLayout {
    ALPHABETIC,      // Main QWERTY keyboard
    SYMBOL_1,        // First symbol page (@#$, etc.)
    SYMBOL_2,        // Math symbols (π√∆, etc.)
    CLIPBOARD,       // Clipboard history view
    FONT_SELECTOR       // Font selector view
}

/**
 * Represents the current state of the shift key
 */
enum class ShiftState {
    OFF,        // Normal lowercase
    SHIFTED,    // Next character will be uppercase
    CAPS_LOCK   // All characters will be uppercase until disabled
}

/**
 * Data class to hold the current state of the keyboard
 */
data class KeyboardState(
    val currentLayout: KeyboardLayout = KeyboardLayout.ALPHABETIC,
    val previousLayout: KeyboardLayout = KeyboardLayout.ALPHABETIC,  // Layout to return to after clipboard
    val shiftState: ShiftState = ShiftState.OFF,
    val lastShiftPressTime: Long = 0L,  // For detecting double-tap on shift
    val selectedFontId: String? = null
)