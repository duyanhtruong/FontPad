package dev.alephany.fontpad.font

import android.content.Context
import dev.alephany.fontpad.state.FontData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.IOException

class FontManager(private val context: Context) {
    private val _fonts = MutableStateFlow<List<FontData>>(emptyList())
    val fonts: StateFlow<List<FontData>> = _fonts.asStateFlow()

    init {
        loadFonts()
    }

    fun loadFonts() {
        val fontDir = getFontDirectory()
        val fontFiles = fontDir.listFiles { file ->
            file.extension.lowercase() in listOf("ttf", "otf")
        } ?: emptyArray()

        val loadedFonts = fontFiles.map { file ->
            FontData(
                id = file.nameWithoutExtension,
                name = file.name,
                filePath = file.absolutePath
            )
        }
        _fonts.value = loadedFonts.sortedBy { it.name }
    }

    private fun getFontDirectory(): File {
        val dir = File(context.filesDir, "fonts")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    suspend fun addFont(fontData: FontData, sourceFile: File) {
        try {
            // Create target file with original name, handling duplicates
            val targetFile = getUniqueFile(getFontDirectory(), fontData.name)
            sourceFile.copyTo(targetFile, overwrite = false)

            // Create new FontData with the actual saved path
            val newFontData = FontData(
                id = targetFile.nameWithoutExtension,
                name = targetFile.name,
                filePath = targetFile.absolutePath
            )

            // Update fonts list
            val currentFonts = _fonts.value.toMutableList()
            currentFonts.add(newFontData)
            _fonts.value = currentFonts.sortedBy { it.name }
        } catch (e: IOException) {
            throw IOException("Failed to save font: ${e.message}")
        }
    }

    suspend fun removeFont(fontData: FontData) {
        try {
            // Delete the font file
            val file = File(fontData.filePath)
            if (file.exists()) {
                file.delete()
            }

            // Update fonts list
            _fonts.value = _fonts.value.filter { it.id != fontData.id }
        } catch (e: IOException) {
            throw IOException("Failed to remove font: ${e.message}")
        }
    }

    private fun getUniqueFile(directory: File, desiredName: String): File {
        var file = File(directory, desiredName)
        var counter = 1

        while (file.exists()) {
            val nameWithoutExtension = desiredName.substringBeforeLast(".")
            val extension = desiredName.substringAfterLast(".", "")
            file = File(directory, "${nameWithoutExtension}_$counter.$extension")
            counter++
        }

        return file
    }
}