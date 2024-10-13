package com.xdivision.techcasts.audioplayer

import android.app.Service
import android.content.Intent
import android.media.session.MediaSession
import android.os.Binder
import android.os.IBinder
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class AudioPlaybackService : Service() {
    private lateinit var mediaSession: MediaSession
    private lateinit var player: ExoPlayer
    private lateinit var notificationManager: AudioNotificationManager

    // Binder given to clients
    private val binder = LocalBinder()

    inner class LocalBinder: Binder() {
        fun getService(): AudioPlaybackService = this@AudioPlaybackService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initializePlayer()
        return START_STICKY
    }

    private fun initializePlayer() {
        player = Exopl
    }
}