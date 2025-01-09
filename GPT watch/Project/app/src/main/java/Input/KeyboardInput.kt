package Input

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text


@Composable
fun ChatInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    focusRequester: FocusRequester
) {
    val focusManager = LocalFocusManager.current // Access the focus manager

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Type...") }, // Placeholder for better usability during testing
        modifier = Modifier
            //.alpha(0f) // Makes the input field completely invisible
            .focusRequester(focusRequester) // Allows it to request focus
            .then (Modifier.offset(y = 5000.dp)) // Moves the field far off-screen
        ,singleLine = true,
        textStyle = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = {
            onSend()
            focusManager.clearFocus() // Clear focus to close the keyboard
        })
    )
}
