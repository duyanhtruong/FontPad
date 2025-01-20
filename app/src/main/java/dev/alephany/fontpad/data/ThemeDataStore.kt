package dev.alephany.fontpad.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.alephany.fontpad.state.KeyboardTheme
import dev.alephany.fontpad.state.ThemeMode
import dev.alephany.fontpad.state.ThemeState
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

class ThemeDataStore(private val context: Context) {
    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val KEYBOARD_THEME = stringPreferencesKey("keyboard_theme")
    }

    val themeState = context.dataStore.data.map { preferences ->
        ThemeState(
            mode = ThemeMode.valueOf(
                preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            ),
            keyboardTheme = KeyboardTheme.valueOf(
                preferences[PreferencesKeys.KEYBOARD_THEME] ?: KeyboardTheme.DEFAULT.name
            )
        )
    }

    suspend fun updateThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun updateKeyboardTheme(theme: KeyboardTheme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEYBOARD_THEME] = theme.name
        }
    }
}