package dev.alephany.fontpad.ui.settings

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.alephany.fontpad.font.FontManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import dev.alephany.fontpad.state.FontData
import kotlinx.coroutines.flow.update

data class FontUiState(
    val fonts: List<FontData> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = false
)

class FontSettingsViewModel(
    private val fontManager: FontManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(FontUiState())
    val uiState: StateFlow<FontUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            fontManager.fonts.collect { fonts ->
                _uiState.update { it.copy(fonts = fonts) }
            }
        }
    }

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
                } ?: throw IllegalArgumentException("Unable to get file name")

                // Verify file extension
                if (!fileName.lowercase().endsWith(".ttf") && !fileName.lowercase().endsWith(".otf")) {
                    throw IllegalArgumentException("Only TTF and OTF files are supported")
                }

                // Create a temporary file
                val tempFile = File(context.cacheDir, fileName)
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(tempFile).use { output ->
                        input.copyTo(output)
                    }
                }

                // Create FontData with original name
                val newFont = FontData(
                    id = tempFile.nameWithoutExtension,
                    name = fileName,
                    filePath = tempFile.absolutePath
                )

                // Add to FontManager (which will handle proper file copying and naming)
                fontManager.addFont(newFont, tempFile)

                // Clean up temp file
                tempFile.delete()

                _uiState.update { it.copy(isLoading = false) }

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
                fontManager.removeFont(fontData)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to remove font: ${e.localizedMessage}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}