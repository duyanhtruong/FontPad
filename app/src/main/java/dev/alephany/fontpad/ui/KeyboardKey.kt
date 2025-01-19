package dev.alephany.fontpad.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

/**
 * A single keyboard key composable that can be used for both regular and special keys.
 *
 * @param text The text to display on the key
 * @param modifier Modifier for the key's layout
 * @param isSpecialKey Whether this is a special key (shift, backspace, etc.)
 * @param isActive Whether the key is in an active state (e.g., shift is active)
 * @param onClick Callback for when the key is clicked
 */
@Composable
fun KeyboardKey(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: Painter? = null,
    isSpecialKey: Boolean = false,
    isActive: Boolean = false,
    onPress: () -> Unit = {},
    onRelease: () -> Unit = {},
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .padding(4.dp)
            .height(48.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        try {
                            onPress()
                            awaitRelease()
                        } finally {
                            onRelease()
                        }
                    },
                    onTap = { onClick() }
                )
            },
        color = when {
            isActive -> MaterialTheme.colorScheme.primary
            isSpecialKey -> MaterialTheme.colorScheme.secondaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        shape = RoundedCornerShape(6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                icon != null -> {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = if (isActive) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                text != null -> {
                    Text(
                        text = text,
                        color = if (isActive) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}