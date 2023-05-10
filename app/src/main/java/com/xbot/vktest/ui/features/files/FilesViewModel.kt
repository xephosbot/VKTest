package com.xbot.vktest.ui.features.files

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.vktest.data.files.FilesRepository
import com.xbot.vktest.model.FileItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val repository: FilesRepository
) : ViewModel() {

    private val state: MutableStateFlow<Pair<String, Int>> = MutableStateFlow(DEFAULT_PATH to DEFAULT_SORT_KEY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val filesList: StateFlow<List<FileItem>> = state.flatMapLatest { (path, sortKey) ->
        repository.getData(path, sortKey)
    }
        .onStart {
            repository.prepareDirectories()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveDirectories(DEFAULT_PATH)
        }
    }

    fun fetchData(path: String, sortKey: Int) = viewModelScope.launch {
        state.update { path to sortKey }
    }

    companion object {
        private val DEFAULT_PATH = Environment.getExternalStorageDirectory().path
        private const val DEFAULT_SORT_KEY = 0
    }
}