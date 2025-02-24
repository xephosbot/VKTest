package com.xbot.api.client

import com.xbot.api.model.VideoApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class VideoApiService(val client: HttpClient) {
    internal suspend inline fun <reified T> request(
        action: HttpClient.() -> HttpResponse
    ): T {
        return client.action().body<T>()
    }
}

suspend fun VideoApiService.getVideos() = request<List<VideoApi>> {
    get(REQUEST_URL)
}

internal const val REQUEST_URL = "https://gist.githubusercontent.com/poudyalanil/ca84582cbeb4fc123a13290a586da925/raw/14a27bd0bcd0cd323b35ad79cf3b493dddf6216b/videos.json"