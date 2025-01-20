package dev.alephany.fontpad

import android.os.Bundle
import android.view.ViewGroup
import android.view.Gravity
import android.widget.EditText
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.core.view.updatePadding

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val editText = EditText(this).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            inputType = EditorInfo.TYPE_CLASS_TEXT or
                    EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
            gravity = Gravity.TOP
            background = null
            updatePadding(left = 16, right = 16, top = 16, bottom = 16)
            setHint("Type here to test custom fonts...")
        }

        setContentView(editText)
    }
}