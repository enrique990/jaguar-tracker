package ni.edu.uam.jaguar_tracker.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun rememberSafeClick(
    delayMillis: Long = 700L,
    onClick: () -> Unit
): () -> Unit {
    var canClick by remember { mutableStateOf(true) }
    val latestClick by rememberUpdatedState(onClick)

    LaunchedEffect(canClick) {
        if (!canClick) {
            delay(delayMillis)
            canClick = true
        }
    }

    return {
        if (canClick) {
            canClick = false
            latestClick()
        }
    }
}