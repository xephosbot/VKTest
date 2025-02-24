package com.xbot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val duration: String,
    val uploadTime: String,
    val views: String,
    val author: String,
    val videoUrl: String,
    val description: String,
    val subscriber: String,
    val isLive: Boolean
)
