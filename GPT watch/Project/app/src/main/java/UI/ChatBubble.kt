package UI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.MaterialTheme
import io.noties.markwon.Markwon

@Composable
fun ChatBubble(text: String, isUser: Boolean) {
    val context = LocalContext.current

    // Initialize Markwon for rendering Markdown
    val markwon = remember { Markwon.create(context) }
    // Preprocess the input LaTeX text
    var processedText = remember { preprocessLatex(text) }

    Box(
        modifier = Modifier
            .fillMaxWidth(1.0f)
            .padding(4.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            color = if (!isUser) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface
        ) {
            AndroidView(
                factory = { ctx ->
                    android.widget.TextView(ctx).apply {
                        setPadding(20, 12, 20, 12) // Inner padding
                        setTextColor(
                            if (isUser) android.graphics.Color.BLACK
                            else android.graphics.Color.WHITE
                        )
                        textSize = 14f
                    }
                },
                update = { textView ->
                    // Preprocess LaTeX formatting locally
                    processedText = preprocessLatex(text)
                    // Render Markdown content
                    markwon.setMarkdown(textView, processedText)
                },
                modifier = Modifier.padding(6.dp) // Padding for TextView
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreviewUser() {
    ChatBubble(
        text = "\\(\\frac{a}{b}\\) Here's an inline equation: \\(E = mc^2\\)\nAnd a fraction: \\(\\frac{a}{b}\\)",
        isUser = true
    )
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreviewAssistant() {
    ChatBubble(
        text = "Evaluate the following definite integral:\n" +
                "\n" +
                "\\[\n" +
                "\\int_{0}^{\\pi} \\sin^2(x) \\, dx\n" +
                "\\]",
        isUser = false
    )
}