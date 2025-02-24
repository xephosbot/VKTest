package com.xbot.player

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.navigation.toRoute
import com.xbot.player.navigation.PlayerRoute
import com.xbot.player.service.PlayerProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class PlayerViewModel(
    playerProvider: PlayerProvider,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val videoUrl: String = savedStateHandle.toRoute<PlayerRoute>().videoUrl
    val controller: StateFlow<Player?> = playerProvider.player
        .onEach { player ->
            val currentMediaItem = player.currentMediaItem
            if (currentMediaItem == null || currentMediaItem.mediaId != videoUrl) {
                with(player) {
                    clearMediaItems()
                    addMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                    prepare()
                    play()
                }
            }
        }
        .catch { error ->
            Log.e("PlayerViewModel", "Error in player occurred", error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null,
        )
}