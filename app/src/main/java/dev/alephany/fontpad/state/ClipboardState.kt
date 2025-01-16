package dev.alephany.fontpad.state

/**
 * Represents the current state of the clipboard functionality in the keyboard.
 * This includes the clipboard history and its current visibility status.
 *
 * @property isVisible Whether the clipboard view is currently visible
 * @property history List of recent clipboard items, with most recent first
 * @property selectedIndex Currently selected item in the clipboard, if any
 */
data class ClipboardState(
    val isVisible: Boolean = false,
    val history: List<ClipboardItem> = emptyList(),
    val selectedIndex: Int? = null
)

/**
 * Represents a single item in the clipboard history.
 * Includes metadata about when the item was copied and its content.
 *
 * @property id Unique identifier for the clipboard item
 * @property content The actual text content of the clipboard item
 * @property timestamp When this item was copied to clipboard
 * @property preview A shortened version of the content for display
 */
data class ClipboardItem(
    val id: String,
    val content: String,
    val timestamp: Long,
    val preview: String = content.generatePreview()
)

/**
 * Extension function to generate a preview of clipboard content.
 * Handles multiline text and long content appropriately.
 */
private fun String.generatePreview(): String = when {
    contains('\n') -> {
        split('\n')
            .take(2)
            .joinToString("\n")
            .let { if (split('\n').size > 2) "$it..." else it }
    }
    length > 100 -> "${take(100)}..."
    else -> this
}