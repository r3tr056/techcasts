package com.xdivision.techcasts.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter


@Composable
fun CoilImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(0.dp)
) {
    Image(
        painer = rememberImagePainter(url),
        contentDescription = contentDescription,
        ContentScale = ContentScale.Crop,
        modifier = modifier.clip(shape)
    )
}