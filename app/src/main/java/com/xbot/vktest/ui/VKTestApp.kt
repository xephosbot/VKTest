package com.xbot.vktest.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xbot.home.navigation.HomeRoute
import com.xbot.home.navigation.homeScreen
import com.xbot.player.navigation.navigateToPlayer
import com.xbot.player.navigation.playerScreen

@Composable
fun VKTestApp(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HomeRoute,
    ) {
        homeScreen(
            onVideoClick = { videoUrl ->
                navController.navigateToPlayer(videoUrl)
            }
        )
        playerScreen()
    }
}