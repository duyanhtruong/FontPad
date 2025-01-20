package dev.alephany.fontpad.ui.settings

import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import dev.alephany.fontpad.R
import dev.alephany.fontpad.state.FontData
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSettingsScreen(
    viewModel: FontSettingsViewModel,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addFont(context, it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Font Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { launcher.launch("*/*") },  // Updated to only accept fonts
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Font") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Test Input Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "TEST FONT INPUT",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            AndroidView(
                                factory = { context ->
                                    EditText(context).apply {
                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                        )
                                        minLines = 3
                                        maxLines = 5
                                        inputType = EditorInfo.TYPE_CLASS_TEXT or
                                                EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE or
                                                EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES or
                                                EditorInfo.TYPE_TEXT_FLAG_AUTO_CORRECT
                                        setBackgroundResource(android.R.color.transparent)
                                        isSingleLine = false

                                        // Determine the current theme
                                        val isDarkTheme = (context.resources.configuration.uiMode and
                                                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                                                android.content.res.Configuration.UI_MODE_NIGHT_YES

                                        val textColor = if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                                        val hintColor = if (isDarkTheme) android.graphics.Color.LTGRAY else android.graphics.Color.DKGRAY

                                        setTextColor(textColor)
                                        setHintTextColor(hintColor)

                                        // Load and tint the icon
                                        val fontIcon = ContextCompat.getDrawable(context, R.drawable.ic_font)?.apply {
                                            setTint(if (isDarkTheme) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
                                            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                                        }

                                        // Set a spannable hint with the tinted icon
                                        val spannableHint = SpannableStringBuilder().apply {
                                            append("- Press the ")
                                            if (fontIcon != null) {
                                                val imageSpan = ImageSpan(fontIcon, ImageSpan.ALIGN_BOTTOM)
                                                append(" ", imageSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                            }
                                            append(" button on top of your keyboard.\n")
                                            append("- Select the font\n")
                                            append("- Type here to test the font")
                                        }

                                        hint = spannableHint
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )


                        }
                    }
                }
                // Error message
                uiState.error?.let { error ->
                    item {
                        ErrorCard(
                            error = error,
                            onDismiss = viewModel::clearError
                        )
                    }
                }

                // Loading indicator
                if (uiState.isLoading) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Empty state
                if (uiState.fonts.isEmpty() && !uiState.isLoading) {
                    item {
                        EmptyState()
                    }
                }

                // Font list
                items(uiState.fonts) { fontData ->
                    FontCard(
                        fontData = fontData,
                        onDelete = { viewModel.removeFont(fontData) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(
    error: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No fonts added yet",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Tap the + button to add a font",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FontCard(
    fontData: FontData,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fontFamily = try {
        val weight = when {
            fontData.name.contains("Bold", ignoreCase = true) -> androidx.compose.ui.text.font.FontWeight.Bold
            fontData.name.contains("Light", ignoreCase = true) -> androidx.compose.ui.text.font.FontWeight.Light
            fontData.name.contains("Medium", ignoreCase = true) -> androidx.compose.ui.text.font.FontWeight.Medium
            fontData.name.contains("Black", ignoreCase = true) -> androidx.compose.ui.text.font.FontWeight.Black
            else -> androidx.compose.ui.text.font.FontWeight.Normal
        }
        FontFamily(Font(File(fontData.filePath), weight = weight))
    } catch (e: Exception) {
        null
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { /* TODO: Font preview/edit */ }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = fontData.name,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete font",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Preview text with actual font if available
            Text(
                text = "The quick brown fox jumps over the lazy dog.",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = fontFamily
            )
        }
    }
}