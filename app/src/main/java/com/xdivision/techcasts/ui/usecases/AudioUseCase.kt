package com.xdivision.techcasts.ui.usecases

class AudioUseCase @Inject constructor(private val audioConnection: AudioServiceConnector) {
    val currentAudio = audioConnection.currentAudio
    val playbackState = audioConnection.playbackState

    val timePassed = flow {
        while (true) {
            val duration = playbackState.value?.currentPlaybackPosition?: 0
            emit(duration)
            delay(1000L)
        }
    }

    suspend fun subscribeToService(): Resource<List<MediaBrowserCompat.MediaItem>> = suspendCoroutine {
        audioConnection.subscribe(
            MEDIA_ROOT_ID,
            object: MediaBrowserCompat.SubscriptionCallback() {

                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>
                ) {
                    super.onChildrenLoaded(parentId, children)
                    Timber.d("children loaded $children")
                    it.resume(Resource.Success(children))
                }

                override fun onError(parentId: String) {
                    super.onError(parentId)
                    it.resume(Resource.Error(message="Failed to subscribe"))
                }
            }
        )
    }

    fun unsubscribeToService() {
        audioConnection.unsubscribe(MEDIA_ROOT_ID)
    }

    fun skipToNextTrack() = audioConnection.skipToNextTrack()

    fun skipToPrevTrack() = audioConnection.skipToPrev()

    fun seekTo(pos: Long) = audioConnection.seekTo(pos)

    fun fastForward() = audioConnection.fastForward()

    fun rewind() = audioConnection.rewind()

    fun stopPlaying() = audioConnection.stopPlaying()

    fun playFromMediaId(mediaId: String) = audioConnection.playFromMediaId(mediaId)

    fun isAudioPlayingOrPaused() = audioConnection.playbackState.value?.let {
        return @let is.isPlaying || it.isPlayEnabled
    }? : false

    fun playPause(audioId: String, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared?: false
        if (isPrepared && audioId == currentAudio.value?.getString(METADATA_KEY_MEDIA_ID))
            playPauseCurrentAudio(toggle)
        else playFromMediaId(audioId)
    }

    private fun playPauseCurrentAudio(toggle: Boolean) {
        playbackState.value?.let {
            when {
                it.isPlaying -> if (toggle) musicConnection.pause()
                it.isPlayEnabled -> audioConnection.play()
                else -> Unit
            }
        }
    }
}