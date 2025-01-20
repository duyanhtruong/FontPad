package dev.alephany.fontpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.alephany.fontpad.data.ThemeDataStore
import dev.alephany.fontpad.state.ThemeMode
import dev.alephany.fontpad.state.ThemeState
import dev.alephany.fontpad.ui.navigation.AppNavigation
import dev.alephany.fontpad.ui.theme.FontPadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val themeDataStore = remember { ThemeDataStore(context) }
            val themeState by themeDataStore.themeState.collectAsState(
                initial = ThemeState(mode = ThemeMode.SYSTEM)
            )

            val systemUiController = rememberSystemUiController()
            val isSystemInDark = isSystemInDarkTheme()

            val isDarkTheme = when (themeState.mode) {
                ThemeMode.SYSTEM -> isSystemInDark
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            FontPadTheme(darkTheme = isDarkTheme) {
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isDarkTheme
                    )
                }

                Surface(color = MaterialTheme.colorScheme.surface) {
                    AppNavigation()
                }
            }
        }
    }
}