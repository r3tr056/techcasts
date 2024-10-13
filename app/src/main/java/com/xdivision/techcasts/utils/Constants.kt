package com.xdivision.techcasts.utils

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "TechcastsChannel"
const val CHANNEL_NAME = "Techcasts"
const val MEDIA_ROOT_ID = "root_id"

const val NETWORK_ERROR = "Network error"
const val DURATION = "duration"

const val sArtworkUri = "content://media/external/audio/albumart"

enum class AudioState {
    PLAYING,
    PAUSED,
    NONE
}