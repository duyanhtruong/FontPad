package dev.alephany.fontpad.clipboard

import android.content.ClipboardManager
import android.content.Context
import dev.alephany.fontpad.state.ClipboardItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Manages clipboard operations for the keyboard.
 * This class wraps the Android ClipboardManager and adds custom functionality
 * such as history management and preview generation.
 *
 * @property context Android context used to access system clipboard
 * @property maxHistorySize Maximum number of items to keep in history
 */
class ClipboardManager(
    private val context: Context,
    private val maxHistorySize: Int = 10
) {
    private val systemClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    private val _history = MutableStateFlow<List<ClipboardItem>>(emptyList())
    val history: StateFlow<List<ClipboardItem>> = _history.asStateFlow()

    init {
        // Initialize with current clipboard content if available
        systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()?.let {
            addToHistory(it)
        }

        // Listen for clipboard changes
        systemClipboard.addPrimaryClipChangedListener {
            systemClipboard.primaryClip?.getItemAt(0)?.text?.toString()?.let {
                addToHistory(it)
            }
        }
    }

    /**
     * Adds new text to the clipboard history.
     * Handles duplicates by moving the existing item to the top if present.
     *
     * @param text The text to add to history
     */
    private fun addToHistory(text: String) {
        _history.value = _history.value
            .filterNot { it.content == text }
            .let { filtered ->
                listOf(
                    ClipboardItem(
                        id = UUID.randomUUID().toString(),
                        content = text,
                        timestamp = System.currentTimeMillis()
                    )
                ) + filtered
            }
            .take(maxHistorySize)
    }

    /**
     * Clears all items from the clipboard history.
     */
    fun clearHistory() {
        _history.value = emptyList()
    }

    /**
     * Copies text to the system clipboard and adds it to history.
     *
     * @param text The text to copy
     */
    fun copyToClipboard(text: String) {
        android.content.ClipData.newPlainText("", text).let {
            systemClipboard.setPrimaryClip(it)
        }
        // History will be updated via the clipboard listener
    }

    /**
     * Deletes a specific item from the clipboard history.
     *
     * @param content The content to delete from history
     */
    fun deleteFromHistory(content: String) {
        _history.value = _history.value.filterNot { it.content == content }
    }
}