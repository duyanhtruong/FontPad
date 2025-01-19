package dev.alephany.fontpad

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.setViewTreeLifecycleOwner
import dev.alephany.fontpad.clipboard.ClipboardManager
import dev.alephany.fontpad.font.FontManager
import dev.alephany.fontpad.state.ShiftState
import dev.alephany.fontpad.ui.Keyboard
import dev.alephany.fontpad.ui.KeyboardAction

/**
 * Input Method Service implementation using Jetpack Compose.
 * Handles keyboard input, clipboard functionality, and manages keyboard state.
 */
class ComposeIMEService : InputMethodService() {
    private val keyboardViewLifecycleOwner = KeyboardViewLifecycleOwner()
    private val fontManager by lazy { FontManager(this) }
    private val clipboardManager by lazy { ClipboardManager(this) }
    private val viewModel by lazy { KeyboardViewModel(clipboardManager, fontManager) }

    override fun onCreate() {
        super.onCreate()
        keyboardViewLifecycleOwner.onCreate()
    }

    override fun onCreateInputView(): View {
        val composeView = ComposeView(this)

        // Make the window draw edge-to-edge
        window?.window?.let { window ->
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Get the WindowInsetsController
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                // Hide system bars in IME mode
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        // Ensure proper lifecycle attachment
        keyboardViewLifecycleOwner.attachToDecorView(window?.window?.decorView)
        composeView.setViewTreeLifecycleOwner(keyboardViewLifecycleOwner)

        composeView.setContent {
            val keyboardState by viewModel.keyboardState.collectAsState()
            val clipboardItems by viewModel.clipboardHistory.collectAsState()
            val fonts by viewModel.fonts.collectAsState()

            // Force recomposition when fonts change
            LaunchedEffect(fonts) {
                Log.d("FontPad", "Fonts updated: ${fonts.size} fonts available")
            }

            MaterialTheme {
                Keyboard(
                    currentLayout = keyboardState.currentLayout,
                    shiftState = keyboardState.shiftState,
                    clipboardItems = clipboardItems,
                    fonts = fonts,
                    selectedFontId = keyboardState.selectedFontId,
                    onKeyClick = { text -> handleTextInput(text, keyboardState.shiftState) },
                    onAction = { action -> handleKeyboardAction(action) },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }

        return composeView
    }

    override fun onWindowShown() {
        super.onWindowShown()
        window?.window?.let { window ->
            // Ensure IME window stays edge-to-edge when shown
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    /**
     * Handles text input considering the current shift state.
     * Consumes shift after typing if not in caps lock.
     */
    private fun handleTextInput(text: String, shiftState: ShiftState) {
        val adjustedText = if (shiftState != ShiftState.OFF) {
            text.uppercase()
        } else {
            text
        }
        currentInputConnection?.commitText(adjustedText, 1)

        // Consume shift after typing a letter
        viewModel.consumeShift()
    }

    /**
     * Handles keyboard actions and routes them appropriately.
     */
    private fun handleKeyboardAction(action: KeyboardAction) {
        when (action) {
            // Input connection actions
            KeyboardAction.Backspace -> {
                // Handle single backspace
                deleteSelection()
            }
            KeyboardAction.StartBackspace -> {
                // Start continuous backspace
                viewModel.startRepeatingBackspace { deleteSelection() }
            }
            KeyboardAction.StopBackspace -> {
                viewModel.stopRepeatingBackspace()
            }
            is KeyboardAction.PasteFromClipboard -> {
                currentInputConnection?.commitText(action.text, 1)
                viewModel.handleAction(action)  // Switch back to previous layout
            }
            KeyboardAction.Enter -> {
                currentInputConnection?.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)
                )
                currentInputConnection?.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER)
                )
            }
            KeyboardAction.Space -> {
                currentInputConnection?.commitText(" ", 1)
            }
            KeyboardAction.Tab -> {
                currentInputConnection?.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_TAB)
                )
                currentInputConnection?.sendKeyEvent(
                    KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_TAB)
                )
            }

            // State management actions (handled by ViewModel)
            KeyboardAction.Shift,
            KeyboardAction.SwitchToAlphabetic,
            KeyboardAction.SwitchToSymbols,
            KeyboardAction.SwitchToSymbols2,
            KeyboardAction.ShowClipboard,
            KeyboardAction.HideClipboard,
            KeyboardAction.ClearClipboard,
            is KeyboardAction.DeleteFromClipboard -> {
                viewModel.handleAction(action)
            }

            // Font selection actions
            KeyboardAction.ShowFontSelector,
            KeyboardAction.HideFontSelector,
            is KeyboardAction.SelectFont -> {
                viewModel.handleAction(action)
            }
        }
    }

    private fun deleteSelection() {
        // First try to delete any selected text
        val selectedText = currentInputConnection?.getSelectedText(0)
        if (!selectedText.isNullOrEmpty()) {
            currentInputConnection?.commitText("", 1)
        } else {
            // If no text is selected, delete previous character
            currentInputConnection?.deleteSurroundingText(1, 0)
        }
    }

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        keyboardViewLifecycleOwner.onResume()
        viewModel.resetState()
        // Explicitly request font list refresh
        viewModel.refreshFonts()
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        keyboardViewLifecycleOwner.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardViewLifecycleOwner.onDestroy()
    }
}