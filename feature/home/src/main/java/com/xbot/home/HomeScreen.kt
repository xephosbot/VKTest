package com.xbot.home

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.xbot.domain.model.Video
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
    onVideoClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContent(
        modifier = modifier,
        state = state.value,
        sideEffect = viewModel.sideEffectFlow,
        onAction = viewModel::onAction,
        onVideoClick = onVideoClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenUiState,
    sideEffect: Flow<HomeScreenSideEffect>,
    onAction: (HomeScreenAction) -> Unit,
    onVideoClick: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    SingleEventEffect(sideEffect) { sideEffect ->
        when (sideEffect) {
            is HomeScreenSideEffect.ShowSnackbar -> {
                val result = snackbarHostState.showSnackbar(
                    message = sideEffect.message,
                    actionLabel = "Retry",
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> onAction(HomeScreenAction.Refresh)
                    else -> Unit
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Videos") },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state is HomeScreenUiState.Loading,
            modifier = Modifier.padding(innerPadding),
            onRefresh = { onAction(HomeScreenAction.Refresh) }
        ) {
            Crossfade(
                targetState = state
            ) { targetState ->
                when (targetState) {
                    is HomeScreenUiState.Loading -> LoadingScreen(modifier)
                    is HomeScreenUiState.Success -> SuccessScreen(
                        modifier = modifier,
                        videos = targetState.videos,
                        onVideoClick = onVideoClick
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SuccessScreen(
    modifier: Modifier = Modifier,
    videos: List<Video>,
    onVideoClick: (String) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(350.dp),
    ) {
        items(videos) { video ->
            VideoItem(video) {
                onVideoClick(video.videoUrl)
            }
        }
    }
}

@Composable
private fun VideoItem(
    video: Video,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    println(video.thumbnailUrl)
    ListItem(
        modifier = modifier.clickable { onClick() },
        leadingContent = {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(128.dp)
                    .aspectRatio(16f / 9f)
            )
        },
        headlineContent = { Text(video.title) },
        supportingContent = { Text(video.description, maxLines = 3, overflow = TextOverflow.Ellipsis) },
    )
}
