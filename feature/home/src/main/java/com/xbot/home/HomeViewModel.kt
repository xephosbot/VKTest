package com.xbot.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.Video
import com.xbot.domain.model.fold
import com.xbot.domain.repository.VideoRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _sideEffectChannel = Channel<HomeScreenSideEffect>(capacity = Channel.BUFFERED)
    val sideEffectFlow: Flow<HomeScreenSideEffect> = _sideEffectChannel.receiveAsFlow()

    private val _state: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(HomeScreenUiState.Loading)
    val state: StateFlow<HomeScreenUiState> = _state
        .onStart { refresh() }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenUiState.Loading
        )

    fun onAction(action: HomeScreenAction) {
        when (action) {
            HomeScreenAction.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.value = HomeScreenUiState.Loading
            videoRepository.getVideos().fold(
                onSuccess = { videos ->
                    _state.value = HomeScreenUiState.Success(videos)
                },
                onFailure = { videos, error ->
                    if (videos != null) {
                        _state.value = HomeScreenUiState.Success(videos)
                    }
                    Log.e(TAG, ERROR_MESSAGE, error)
                    showSnackbar(error.message ?: "Unknown error")
                }
            )
        }
    }

    private fun showSnackbar(message: String) {
        _sideEffectChannel.trySend(HomeScreenSideEffect.ShowSnackbar(message))
    }

    companion object {
        const val TAG = "HomeViewModel"
        const val ERROR_MESSAGE = "Error refreshing videos"
    }
}

sealed interface HomeScreenUiState {
    data object Loading : HomeScreenUiState
    data class Success(val videos: List<Video>) : HomeScreenUiState
}

sealed interface HomeScreenAction {
    data object Refresh : HomeScreenAction
}

sealed interface HomeScreenSideEffect {
    data class ShowSnackbar(val message: String) : HomeScreenSideEffect
}