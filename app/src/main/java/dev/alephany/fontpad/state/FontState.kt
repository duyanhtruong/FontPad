package dev.alephany.fontpad.state

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import java.io.File

data class FontData(
    val id: String,
    val name: String,
    val filePath: String
) {
    fun loadFont(): FontFamily? = try {
        FontFamily(Font(File(filePath)))
    } catch (e: Exception) {
        null
    }
}