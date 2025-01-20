package dev.alephany.fontpad.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alephany.fontpad.data.ThemeDataStore
import dev.alephany.fontpad.state.KeyboardTheme
import dev.alephany.fontpad.state.ThemeMode
import dev.alephany.fontpad.state.ThemeState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThemeSettingsViewModel(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    val themeState: StateFlow<ThemeState> = themeDataStore.themeState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeState()
        )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            themeDataStore.updateThemeMode(mode)
        }
    }

    fun setKeyboardTheme(theme: KeyboardTheme) {
        viewModelScope.launch {
            themeDataStore.updateKeyboardTheme(theme)
        }
    }
}