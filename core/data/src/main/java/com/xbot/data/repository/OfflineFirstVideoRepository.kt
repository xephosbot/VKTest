package com.xbot.data.repository

import com.xbot.api.client.VideoApiService
import com.xbot.api.client.getVideos
import com.xbot.api.model.VideoApi
import com.xbot.data.asEntity
import com.xbot.data.asExternalModel
import com.xbot.data.model.VideoEntity
import com.xbot.data.source.VideoDao
import com.xbot.domain.model.Result
import com.xbot.domain.model.Video
import com.xbot.domain.repository.VideoRepository

internal class OfflineFirstVideoRepository(
    private val videoDao: VideoDao,
    private val videoApiService: VideoApiService,
) : VideoRepository {
    override suspend fun getVideos(): Result<List<Video>> {
        return try {
            Result.Success(getVideosNetwork())
        } catch (error: Throwable) {
            Result.Failure(getVideosLocal(), error)
        }
    }

    private suspend fun getVideosNetwork(): List<Video> {
        val videos = videoApiService.getVideos()
        videoDao.clearVideos()
        videoDao.upsertVideos(videos.map(VideoApi::asEntity))
        return videoDao.getVideos().map(VideoEntity::asExternalModel)
    }

    private suspend fun getVideosLocal(): List<Video> {
        return videoDao.getVideos().map(VideoEntity::asExternalModel)
    }
}