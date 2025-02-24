package com.xbot.domain.repository

import com.xbot.domain.model.Result
import com.xbot.domain.model.Video

interface VideoRepository {
    suspend fun getVideos(): Result<List<Video>>
}