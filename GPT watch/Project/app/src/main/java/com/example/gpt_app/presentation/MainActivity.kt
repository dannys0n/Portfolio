/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */
package com.example.gpt_app.presentation

import UI.ChatScreen
import android.os.Bundle
import android.view.InputDevice
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.scrollBy
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import com.example.gpt_app.presentation.theme.GPT_APPTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var listState: ScalingLazyListState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            listState = rememberScalingLazyListState() // Initialize listState here
            GPT_APPTheme {
                ChatScreen(listState)
            }
        }
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        if (event != null &&
            event.action == MotionEvent.ACTION_SCROLL &&
            event.isFromSource(InputDevice.SOURCE_ROTARY_ENCODER)
        ) {
            val delta = event.getAxisValue(MotionEvent.AXIS_SCROLL)
            val targetDelta = -delta * 150 // Adjust multiplier for sensitivity

            lifecycleScope.launch {
                val steps = 10 // Number of animation steps
                val stepSize = targetDelta / steps
                repeat(steps) {
                    listState.scrollBy(stepSize)
                    delay(16L) // Approx. 60 FPS
                }
            }

            return true // Event handled
        }
        return super.onGenericMotionEvent(event)
    }
}
