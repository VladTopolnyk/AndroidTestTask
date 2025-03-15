package app.bettermetesttask.movies.sections.compose.common

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

@SuppressLint("ModifierFactoryUnreferencedReceiver")
inline fun Modifier.clickableWithoutRippleEffect(
    enabled: Boolean = true,
    crossinline onClick: () -> Unit
): Modifier =
    composed {
        clickable(
            enabled = enabled,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }