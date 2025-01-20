package dev.alephany.fontpad.state

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class KeyboardTheme {
    DEFAULT,  // Light theme
    DARK
}

data class ThemeState(
    val mode: ThemeMode = ThemeMode.SYSTEM,
    val keyboardTheme: KeyboardTheme = KeyboardTheme.DEFAULT
)