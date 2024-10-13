package com.xdivision.techcasts.data.models.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "albums", indices = [(Index(value = ["id"], unique = true))])
data class Album(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name="artist") val artist: String,
    )
{

}
