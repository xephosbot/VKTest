package com.xbot.player.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.activity
import com.xbot.player.PlayerActivity
import kotlinx.serialization.Serializable

@Serializable
data class PlayerRoute(val videoUrl: String)

fun NavHostController.navigateToPlayer(videoUrl: String) = navigate(PlayerRoute(videoUrl))

fun NavGraphBuilder.playerScreen() {
    activity<PlayerRoute> {
        activityClass = PlayerActivity::class
    }
}