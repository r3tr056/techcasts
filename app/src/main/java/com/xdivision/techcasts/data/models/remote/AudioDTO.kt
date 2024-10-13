package com.xdivision.techcasts.data.models.remote

data class AudioDTO(
    val id: String = "",
    val title: String = "",
    val duration: Long = 0L,
    val artists: List<String> = emptyList(),
    val imageUrl: String = "",
    val musicUrl: String = ""
)