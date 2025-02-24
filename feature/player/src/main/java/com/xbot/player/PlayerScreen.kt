package com.xbot.player

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.view.WindowInsetsControllerCompat
import com.github.fengdai.compose.media.Media
import com.github.fengdai.compose.media.ResizeMode
import com.github.fengdai.compose.media.ShowBuffering
import com.github.fengdai.compose.media.SurfaceType
import com.github.fengdai.compose.media.rememberMediaState
import com.xbot.player.component.PlayerController
import com.xbot.player.utils.rememberSystemUiController

@Composable
internal fun PlayerScreen(
    player: Player?,
    onCloseClick: () -> Unit,
) {
    player?.let {
        PlayerScreenContent(
            player = it,
            onCloseClick = onCloseClick,
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun PlayerScreenContent(
    player: Player,
    onCloseClick: () -> Unit,
) {
    val state = rememberMediaState(player = player)
    val systemUiController = rememberSystemUiController()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    SideEffect {
        systemUiController.isSystemBarsVisible = !isLandscape
        systemUiController.systemBarsBehavior = if (isLandscape) {
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        }
    }


    CompositionLocalProvider(LocalContentColor provides Color.White) {
        Media(
            state = state,
            modifier = Modifier.fillMaxSize().background(Color.Black),
            surfaceType = SurfaceType.SurfaceView,
            resizeMode = ResizeMode.Fit,
            keepContentOnPlayerReset = false,
            useArtwork = true,
            showBuffering = ShowBuffering.Always,
            buffering = {
                Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator(color = LocalContentColor.current)
                }
            }
        ) { mediaState ->
            val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

            PlayerController(
                modifier = Modifier.fillMaxSize(),
                mediaState = mediaState,
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                bottomActions = {
                    IconButton(onClick = {
                        if (isLandscape) {
                            systemUiController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                        } else {
                            systemUiController.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                        }
                    }) {
                        Icon(
                            imageVector = when (isLandscape) {
                                true -> Icons.Default.FullscreenExit
                                else -> Icons.Default.Fullscreen
                            },
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    }
}