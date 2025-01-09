package Input

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.util.*

/**
 * Speech-to-Text functionality encapsulated as a composable function.
 * This provides a launcher for the speech recognition intent.
 *
 * @param onResult Callback invoked when the speech recognition completes successfully.
 */
@Composable
fun rememberSpeechToTextLauncher(onResult: (String) -> Unit) =
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val recognizedText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            recognizedText?.let { onResult(it) }
        }
    }

/**
 * Creates an intent for launching the Speech-to-Text recognizer.
 *
 * @return The configured intent.
 */
fun createSpeechToTextIntent(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
    }
}