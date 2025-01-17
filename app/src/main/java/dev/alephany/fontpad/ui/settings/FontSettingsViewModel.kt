package dev.alephany.fontpad.ui.settings

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

data class FontUiState(
    val fonts: List<FontData> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

data class FontData(
    val id: String,
    val name: String,
    val filePath: String
)

class FontSettingsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FontUiState())
    val uiState: StateFlow<FontUiState> = _uiState.asStateFlow()

    fun addFont(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Get file name
                val fileName = context.contentResolver.query(
                    uri,
                    arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
                    null, null, null
                )?.use { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(0)
                } ?: "Unknown Font"

                // Verify file extension
                if (!fileName.lowercase().endsWith(".ttf") && !fileName.lowercase().endsWith(".otf")) {
                    throw IllegalArgumentException("Only TTF and OTF files are supported")
                }

                // Create a file in app's font directory
                val fontDir = File(context.filesDir, "fonts").apply { mkdirs() }
                val fontFile = File(fontDir, "font_${System.currentTimeMillis()}_$fileName")

                // Copy file content
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(fontFile).use { output ->
                        input.copyTo(output)
                    }
                }

                // Add to state
                val newFont = FontData(
                    id = fontFile.nameWithoutExtension,
                    name = fileName,
                    filePath = fontFile.absolutePath
                )

                _uiState.value = _uiState.value.copy(
                    fonts = _uiState.value.fonts + newFont,
                    isLoading = false
                )

            } catch (e: Exception) {
                Log.e("FontSettings", "Error adding font", e)
                _uiState.value = _uiState.value.copy(
                    error = e.localizedMessage ?: "Failed to add font",
                    isLoading = false
                )
            }
        }
    }

    fun removeFont(fontData: FontData) {
        viewModelScope.launch {
            try {
                // Delete the font file
                File(fontData.filePath).delete()

                // Remove from state
                _uiState.value = _uiState.value.copy(
                    fonts = _uiState.value.fonts.filter { it.id != fontData.id }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to remove font: ${e.localizedMessage}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}