package com.xdivision.techcasts.ui.screens.home

import com.xdivision.techcasts.data.models.local.Audio
import com.xdivision.techcasts.utils.AudioState
import com.xdivision.techcasts.utils.AudioState.PAUSED
import com.xdivision.techcasts.utils.AudioState.PLAYING

data class HomeScreenState(
    val audioList: List<Audio> = emptyList(),
    val currentPlayingAudio: Audio? = null,
    val searchBarText: String = "",
    val audioState: AudioState = AudioState.NONE
) {
    val isAudioBottomBarVisible = currentPlayingAudio != null && (audioState == PLAYING || audioState == PAUSED)
    val isAudioPlaying = audioState = PLAYING
}
