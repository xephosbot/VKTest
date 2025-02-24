package com.xbot.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.xbot.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeScreen(
    onVideoClick: (String) -> Unit
) {
    composable<HomeRoute> {
        HomeScreen(
            onVideoClick = onVideoClick
        )
    }
}