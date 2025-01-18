package dev.alephany.fontpad.ui.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.alephany.fontpad.ui.KeyboardKey
import dev.alephany.fontpad.R

/**
 * Top bezel of the keyboard containing the clipboard button and spacing.
 * This is a common component used across all keyboard layouts.
 *
 * @param onClipboardClick Callback when the clipboard button is clicked
 * @param modifier Optional modifier for the bezel
 */
@Composable
internal fun KeyboardBezel(
    onClipboardClick: () -> Unit,
    onFontSelectorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Clipboard button
        KeyboardKey(
            icon = painterResource(id = R.drawable.ic_clipboard),
            modifier = Modifier.size(48.dp),
            isSpecialKey = true,
            onClick = onClipboardClick
        )

        // Spacing between buttons
        Spacer(modifier = Modifier.width(4.dp))

        // Font selector button
        KeyboardKey(
            icon = painterResource(id = R.drawable.ic_font),
            modifier = Modifier.size(48.dp),
            isSpecialKey = true,
            onClick = onFontSelectorClick
        )

        // Spacer to fill remaining width
        Spacer(modifier = Modifier.weight(1f))
    }
}