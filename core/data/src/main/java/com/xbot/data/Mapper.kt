package com.xbot.data

import com.xbot.api.model.VideoApi
import com.xbot.data.model.VideoEntity
import com.xbot.domain.model.Video

internal fun VideoApi.asEntity() = VideoEntity(
    id = id.toInt(),
    title = title,
    thumbnailUrl = thumbnailUrl,
    duration = duration,
    uploadTime = uploadTime,
    views = views,
    author = author,
    videoUrl = videoUrl,
    description = description,
    subscriber = subscriber,
    isLive = isLive
)

internal fun VideoEntity.asExternalModel() = Video(
    id = id,
    title = title,
    thumbnailUrl = thumbnailUrl,
    duration = duration,
    uploadTime = uploadTime,
    views = views,
    author = author,
    videoUrl = videoUrl,
    description = description,
    subscriber = subscriber,
    isLive = isLive
)
