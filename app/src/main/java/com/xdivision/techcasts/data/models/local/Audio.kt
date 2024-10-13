package com.xdivision.techcasts.data.models.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audio_table")
data class Audio(
    @PrimaryKey
    val id: String,
    val title: String,
    val duration: Long,
    val artists: List<String>,
    val imageUrl: String,
    val musicUrl: String
)