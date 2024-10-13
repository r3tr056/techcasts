package com.xdivision.techcasts.datasource

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.exoplayer.datasource.MusicPlayerDataSource.State.*
import dev.vaibhav.musicx.utils.DURATION

abstract class AudioPlayerDataSource {

    var allAudio: List<Audio> = emptyList()

    val allAudioAsMetadata: List<MediaMetadataCompat>
        get() = allAudio.map { getMetadataCompatFromAudio(it) }

    val allAudioAsMediaItem: List<MediaBrowserCompat.MediaItem>
        get() = allAudio.map { getMediaItemFromAudio(it) }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    protected var state: State = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach {
                        it(value == STATE_INITIALIZED)
                    }
                }
            } else field = value
        }

    abstract suspend fun getAudio()

    private fun getMetadataCompatFromAudio(audio: Audio): MediaMetadataCompat = MediaMetadataCompat.Builder().apply {
        putString(MediaMetadataCompat.METADATA_KEY_TITLE, audio.title)
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, audio.title)
        putString(MediaBrowserCompat.METADATA_KEY_MEDIA_ID, audio.id)
        putString(MediaBrowserCompat.METADATA_KEY_ALBUM_ART_URI, audio.imageUrl)
        putString(MediaBrowserCompat.METADATA_KEY_ARTIST, audio.artists.joinToString(","))
        putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, audio.artists.joinToString(","))
        putLong(MediaMetadataCompat.METADATA_KEY_DURATION, audio.duration)
        putString(MediaBrowserCompat.METADATA_KEY_DISPLAY_ICON_URI, audio.imageUrl)
        putString(MediaBrowserCompat.METADATA_KEY_DISPLAY_DESCRIPTION, audio.artists.joinToString(","))
        putString(MediaBrowserCompat.METADATA_KEY_MEDIA_URI, audio.audioUrl)
    }.build()

    private fun getMediaItemFromAudio(audio: Audio) = MediaBrowserCompat.MediaItem(
        MediaDescriptionCompat.Builder().apply {
            setMediaUri(audio.audioUrl.toUri())
            setTitle(audio.title)
            setSubtitle(audio.artists.joinToString(","))
            setMediaId(audio.id)
            setIconUri(audio.imageUrl.toUri())
            setExtras(bundleOf(DURATION to audio.duration))
        }.build(),
        MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
    )

    fun asMediaSource(dataSourceFactory: DefaultDataSource.Factory): MediaSource {
        val mediaSources = allAudio.map {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(it.audioUrl.toUri())
        }
        return ConcatenatingMediaSource().apply { addMediaSources(mediaSources) }
    }

    fun whenReady(onReady: (Boolean) -> Unit): Boolean = 
        if (state == STATE_CREATED || state == STATE_INITIALIZED) {
            onReadyListeners += onReady
            false
        } else {
            onReady(state == STATE_INITIALIZED)
            true
        }

    enum class State {
        STATE_CREATED,
        STATE_INITIALIZED,
        STATE_INITIALIZING,
        STATE_ERROR
    }
}