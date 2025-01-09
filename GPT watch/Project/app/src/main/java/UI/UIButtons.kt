package UI

import Input.createSpeechToTextIntent
import Input.rememberSpeechToTextLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme


@Composable
fun InputButtons(
    onKeyboardInput: () -> Unit,
    onVoiceInput: (String) -> Unit
) {
    // Use the Speech-to-Text launcher
    val speechRecognizerLauncher = rememberSpeechToTextLauncher(onVoiceInput)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Mic Button for Speech-to-Text
        IconButton(
            onClick = {
                val intent = createSpeechToTextIntent()
                speechRecognizerLauncher.launch(intent)
            },
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = MaterialTheme.colors.onSecondary,
                    shape = MaterialTheme.shapes.small
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.MicNone,
                contentDescription = "Voice Input",
                tint = MaterialTheme.colors.onSurface
            )
        }

        // Keyboard Input Button
        IconButton(
            onClick = onKeyboardInput,
            modifier = Modifier.size(64.dp) // Adjust size to match the circular background
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp) // Circle size
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onSecondary), // Circle background
                contentAlignment = Alignment.Center // Center the icon
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "Keyboard Input",
                    tint = MaterialTheme.colors.onSurface // Icon tint
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InputButtonsPreview() {
    InputButtons(
        onKeyboardInput = { /* Simulate keyboard input */ },
        onVoiceInput = { /* Simulate voice input */ }
    )
}