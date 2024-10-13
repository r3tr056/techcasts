package com.xdivision.techcasts.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.xdivision.techcasts.ui.theme.TechcastsTheme


@Composable
fun AudioItem(
    audio: Audio,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.surface,
    shape: Shape = RoundedCornerShape(8.dp),
    onItemClick: (Audio) -> Unit
) {
    Surface(
        onClick = {
            onItemClick(audio)
        },
        shape = shape,
        modifier = modifier,
        color = color,
        tonalElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(audio.coverImageUrl),
                contentDescription = "Audio cover",
                modifier = Modifier
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = audio.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWt = FontWeight.Medium),
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(0.dp))
                Text(
                    text = audio.artists.getArtistsString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// Previews
@Preview
@Composable
private fun AudioItemPreview() {
    TechcastsTheme {
        AudioItem(
            audio = sampleMusicList[0],
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            color = MaterialTheme.colors.surface
        ) {

        }
    }
}