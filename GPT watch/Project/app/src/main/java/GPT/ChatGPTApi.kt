package GPT

import androidx.annotation.Keep
import com.example.gpt_app.BuildConfig
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data classes for request and response
@Keep
data class ChatRequest(
    @SerializedName("model") val model: String,
    @SerializedName("messages") val messages: List<Message>
)

@Keep
data class ChatResponse(
    @SerializedName("choices") val choices: List<Choice>
)

@Keep
data class Choice(
    @SerializedName("message") val message: Message
)

@Keep
data class Message(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

// Global object to provide the current model
@Keep
object ModelProvider {
    fun getModel(): String {
        return when (BuildConfig.CHAT_MODEL) {
            1 -> "gpt-3.5-turbo"
            2 -> "gpt-4o"
            else -> "gpt-4o"
        }
    }
}

// Define the ChatGPT API interface
@Keep
interface ChatGPTApi {
    @Headers("Authorization: Bearer (API key here)")
    @POST("v1/chat/completions")
    suspend fun getResponse(@Body request: ChatRequest): ChatResponse
}

// Singleton object to hold the Retrofit client and API instance
@Keep
object ChatGPTApiClient {
    private const val BASE_URL = "https://api.openai.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ChatGPTApi = retrofit.create(ChatGPTApi::class.java)
}

// Singleton object to manage the chat session
@Keep
object ChatGPTSession {
    private val messages = mutableListOf<Message>() // Keeps track of the conversation history

    /**
     * Sends a message to ChatGPT and retains the context.
     *
     * @param userMessage The message from the user.
     * @return The response from ChatGPT.
     */
    suspend fun sendMessage(userMessage: String): String {
        // Add the user's message to the conversation
        messages.add(Message(role = "user", content = userMessage))

        return try {
            // Create the ChatRequest object with the full message history
            val request = ChatRequest(
                //model = "gpt-4o",
                //model = "gpt-3.5-turbo",
                model = ModelProvider.getModel(),
                messages = messages
            )

            // Make the API call
            val response = ChatGPTApiClient.api.getResponse(request)

            // Extract the ChatGPT response and add it to the message history
            val chatGPTResponse = response.choices.firstOrNull()?.message?.content ?: "No response"
            messages.add(Message(role = "assistant", content = chatGPTResponse))

            // Return the response to the caller
            chatGPTResponse
        } catch (e: Exception) {
            // Handle errors and return a message
            val errorResponse = "Error: ${e.message}"
            messages.add(Message(role = "assistant", content = errorResponse))
            errorResponse
        }
    }

    /**
     * Clears the chat session.
     */
    fun clearSession() {
        messages.clear()
    }
}

// Mock implementation for testing without the API
object MockChatGPT {
    fun getMockResponse(userMessage: String): String {
        return when (userMessage) {
            "CHATGPT?" -> "I am an AI model trained by OpenAI to assist with a variety of tasks, including answering questions and generating text."
            "Tell me a joke!" -> "Why did the computer show up at work late? It had a hard drive!"
            "What's the capital of France?" -> "The capital of France is Paris."
            else -> "I'm not sure about that, but feel free to ask me something else!"
        }
    }
}