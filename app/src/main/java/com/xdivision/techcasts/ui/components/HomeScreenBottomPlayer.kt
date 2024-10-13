package com.xdivision.techcasts.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MusicBottomBar(
    audio: Audio,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (Audio) -> Unit,
    onPlayPauseButtonPressed: (Audio) -> Unit
) {
    Surface(
        onClick={ onItemClick(audio) },
        tonalElevation=4.dp,
        shadowElevation=4.dp,
        modifier=modfier.testTag(BOTTOM_BAR_TAG),
        shape=RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment=Alignment.CenterVertically,
            modifier=Modifier.padding(8.dp)
        ) { 
            // AudioArt
            CoilImage(
                url = music.imageUrl,
                contentDescription="AudioPoster",
                shape=RoundedCornerShape(10.dp),
                modfier=Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )

            Spacer(modifier=Modifier.width(16.dp))
            Column(
                modifier=Modifier
                    .weight(1f)
            ) {
                // Title text
                Text(
                    text=audio.title,
                    style=MaterialTheme.typography.titleMedium.copy(fontWeight=FontWeight.Medium),
                    color=MaterialTheme.colors.onSurface,
                    maxLines=1,
                    overflow=TextOverflow.Ellipsis
                )
                // Spacer
                Spacer(modifier=Modifier.height(0.dp))
                // Artist Names
                Text(
                    text=audio.artists.getArtistsString(),
                    style=MaterialTheme.typography.labelSmall,
                    overflow=TextOverflow.Ellipsis,
                    maxLines = 1,
                    color=MaterialTheme.colors.onSurface.copy(alpha=0.6f)
                )
            }
            // PlayPause Button of bottom player bar
            PlayPauseButton(
                audio=audio,
                isPlaying=isPlaying,
                onPlayPauseButtonPressed=onPlayPauseButtonPressed
            )
        }
    }
}

@Composable
fun AddAudioFab(modifier: Modifier=Modifier, onClick: () -> Unit) {
    LargeFloatingActionButton(
        onClick=onClick,
        modifier=modifier
    ) {
        Icon(
            imageVector=Icons.Rounded.Add,
            contentDescription=stringResource(R.string.add_audio_cd)
        )
    }
}