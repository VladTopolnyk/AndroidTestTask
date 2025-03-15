package app.bettermetesttask.movies.sections.compose.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import app.bettermetesttask.movies.sections.compose.theme.ShimmerItemColor

@Composable
fun ShimmerItem(
    width: Dp,
    height: Dp = width,
    shape: Shape,
    color: Color = ShimmerItemColor
) {
    Box(
        Modifier
            .size(width = width, height = height)
            .clip(shape)
            .shimmerEffect(color)
    )
}