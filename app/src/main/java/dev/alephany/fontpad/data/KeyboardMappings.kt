package dev.alephany.fontpad.data

/**
 * Object containing all keyboard mappings and layouts.
 * Organizes keys by keyboard type (Alphabetic/Symbols) and provides
 * corresponding key weights for special keys.
 */
object KeyboardMappings {
    // Common rows
    val numberRow = (1..9).map { it.toString() } + "0"

    object Alphabetic {
        val topRow = listOf("q", "w", "e", "r", "t", "y", "u", "i", "o", "p")
        val middleRow = listOf("a", "s", "d", "f", "g", "h", "j", "k", "l")
        val bottomRow = listOf("shift", "z", "x", "c", "v", "b", "n", "m", "⌫")
        val actionRow = listOf("tab", "?123", "space", ",", ".", "enter")

        // Special key weights for the alphabetic layout
        val keyWeights = mapOf(
            "shift" to 1.5f,    // Shift key (wider)
            "⌫" to 1.5f,       // Backspace (wider)
            "tab" to 1.5f,      // Tab key (wider)
            "?123" to 1.5f,     // Symbol switch (wider)
            "space" to 4f,      // Space bar (widest)
            "enter" to 1.5f     // Enter key (wider)
        )
    }

    object Symbols {
        // Page 1 - Common symbols (@#$, etc.)
        val topSymbolRow1 = listOf("@", "#", "$", "_", "&", "-", "+", "(", ")", "/")
        val middleSymbolRow1 = listOf("*", "\"", "'", ":", ";", "!", "?", "⌫")
        val actionSymbolRow1 = listOf("ABC", "=\\<", "space", ",", ".", "enter")

        // Page 2 - Math and special symbols
        val topSymbolRow2 = listOf("~", "`", "|", "•", "√", "π", "÷", "×", "§", "∆")
        val middleSymbolRow2 = listOf("£", "€", "¥", "^", "°", "=", "{", "}", "\\")
        val bottomSymbolRow2 = listOf("%", "©", "®", "™", "✓", "[", "]", "⌫")
        val actionSymbolRow2 = listOf("ABC", "?123", "space", "<", ">", "enter")

        // Special key weights for both symbol layouts
        val keyWeights = mapOf(
            "ABC" to 1.5f,      // Switch to alphabetic (wider)
            "=\\<" to 1.5f,     // Switch to second symbol page (wider)
            "?123" to 1.5f,     // Switch to first symbol page (wider)
            "⌫" to 1.5f,        // Backspace (wider)
            "space" to 4f,      // Space bar (widest)
            "enter" to 1.5f     // Enter key (wider)
        )
    }
}