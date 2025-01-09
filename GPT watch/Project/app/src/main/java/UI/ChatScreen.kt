package UI

import GPT.ChatGPTSession
import GPT.ModelProvider
import Input.ChatInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import kotlinx.coroutines.launch


// Define the data class for chat messages
data class Message(val role: String, val content: String)

@Composable
fun ChatScreen(
    listState: ScalingLazyListState
) {
    var messages by remember { mutableStateOf(listOf(Message("assistant", ModelProvider.getModel()))) }
    var userInput by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val inputFieldFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        timeText = { TimeText() },
        positionIndicator = {
            androidx.wear.compose.material.PositionIndicator(scalingLazyListState = listState)
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display chat messages
            items(messages) { message ->
                ChatBubble(
                    text = message.content,
                    isUser = message.role == "user"
                )
            }

            // Add buttons for input methods
            item {
                InputButtons(
                    onKeyboardInput = {
                        // Clear focus first, then regain focus
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            inputFieldFocusRequester.requestFocus() // Focus the input field
                        }
                    },
                    onVoiceInput = { recognizedText ->
                        // Handle recognized text as user input
                        val userMessage = Message("user", recognizedText)
                        messages = messages + userMessage

                        // Add loading message
                        messages = messages + Message("assistant", "Loading...")

                        coroutineScope.launch {
                            try {
                                val responseContent = ChatGPTSession.sendMessage(recognizedText)

                                // Remove the loading message and add the response
                                messages = messages.dropLast(1) + Message("assistant", responseContent)
                            } catch (e: Exception) {
                                val errorMessage = Message("assistant", "Error: ${e.message}")
                                messages = messages.dropLast(1) + errorMessage
                            } finally {
                                listState.scrollToItem(messages.size - 1)
                            }
                        }
                    }
                )
            }
        }

        // Auto-scroll to the bottom when messages are updated
        LaunchedEffect(messages.size) {
            coroutineScope.launch {
                listState.scrollToItem(messages.size - 1)
            }
        }
    }

    // Functionally exists, but invisible to UI
    ChatInputField(
        value = userInput,
        onValueChange = { userInput = it },
        onSend = {
            if (userInput.isNotBlank()) {
                val userMessage = Message("user", userInput)
                messages = messages + userMessage

                // Add loading message
                messages = messages + Message("assistant", "Loading...")

                coroutineScope.launch {
                    try {
                        // Example assistant response for testing
                        val responseContent = ChatGPTSession.sendMessage(userInput)

                        // Remove the loading message and add the response
                        messages = messages.dropLast(1) + Message("assistant", responseContent)
                    } catch (e: Exception) {
                        val errorMessage = Message("assistant", "Error: ${e.message}")
                        messages = messages.dropLast(1) + errorMessage
                    } finally {
                        userInput = ""
                        listState.scrollToItem(messages.size - 1)
                    }
                }
            }
        },
        focusRequester = inputFieldFocusRequester
    )
}