package dev.alephany.fontpad.ui.keyboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.alephany.fontpad.state.ClipboardItem

/**
 * Displays clipboard history and provides controls for clipboard operations.
 * Shows a list of clipboard items with options to paste or clear history.
 * Height is constrained to match keyboard layout size.
 *
 * @param items List of clipboard items to display
 * @param onPaste Callback when an item is selected for pasting
 * @param onClear Callback to clear clipboard history
 * @param onDismiss Callback to dismiss clipboard view
 * @param modifier Optional modifier for the clipboard view
 */
@Composable
fun ClipboardView(
    items: List<ClipboardItem>,
    onPaste: (String) -> Unit,
    onDelete: (String) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 240.dp)
            .heightIn(max = 240.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Header with title and actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Clipboard History",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(
                        onClick = onClear,
                        enabled = items.isNotEmpty()
                    ) {
                        Text("Clear")
                    }
                    TextButton(onClick = onDismiss) {
                        Text("Done")
                    }
                }
            }

            if (items.isEmpty()) {
                // Empty state display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items in clipboard history",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Scrollable list of clipboard items
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(items) { item ->
                        ClipboardItemCard(
                            item = item,
                            onPaste = { onPaste(item.content) },
                            onDelete = { onDelete(item.content) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual clipboard item card showing content preview and action buttons.
 * Displays up to 2 lines of content with ellipsis for longer text.
 *
 * @param item The clipboard item to display
 * @param onPaste Callback when paste is requested for this item
 * @param onDelete Callback when delete is requested for this item
 * @param modifier Optional modifier for the card
 */
@Composable
private fun ClipboardItemCard(
    item: ClipboardItem,
    onPaste: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onPaste
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Preview text with ellipsis for long content
            Text(
                text = item.preview,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Delete button
                TextButton(
                    onClick = onDelete,
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }

                // Paste button
                TextButton(
                    onClick = onPaste,
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("Paste")
                }
            }
        }
    }
}