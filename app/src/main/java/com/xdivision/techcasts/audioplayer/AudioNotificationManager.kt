package com.xdivision.techcasts.audioplayer

import android.app.PendingIntent
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager

import com.xdivision.techcasts.utils.CHANNEL_ID
import com.xdivision.techcasts.utils.NOTIFICATION_ID
import com.xdivision.techcasts.utils.loadImageBitmap


class AudioNotificationManager(private val audioService: AudioService,
    sessionToken: MediaSessionCompat.Token,
    private val onAudioChanged: () -> Unit
) {
    private val mediaController = MediaControllerCompat(musicService, sessionToken)

    private val notificationManager = PlayerNotificationManager.Builder(
        audioService,
        NOTIFICATION_ID,
        CHANNEL_ID
    ).apply {
        setChannelNameResourceId(R.string.notification_channel_name)
        setChannelDescriptionResourceId(R.string.notification_channel_description)
        setMediaDescriptionAdapter(AudioMediaDescriptionAdapter(mediaController))
        setNotificationListener(AudioNotificationListener(audioService))
    }.build().apply {
        setMediaSessionToken(sessionToken)
    }

    fun showNotification(palyer: Player) {
        notificationManager.setPlayer(player)
    }

    private inner class AudioMediaDescriptionAdapter(private val mediaController: MediaControllerCompat) : PlayerNotificationManager.MediaDescriptionAdapter {

        override fun getCurrentContentTitle(player: Player): CharSequence {
            onAudioChanged()
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {
            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence {
            return mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargetIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ) : Bitmap? {
            musicService.loadImageBitmap(mediaController.metadata.description.iconUri.toString()) {
                callback.onBitmap(it)
            }
            return null
        }
    }
}