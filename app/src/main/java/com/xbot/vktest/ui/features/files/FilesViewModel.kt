package com.xbot.vktest.ui.features.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.vktest.data.FilesRepository
import com.xbot.vktest.model.FileItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val repository: FilesRepository
) : ViewModel() {

    fun getFilesList(path: String): StateFlow<List<FileItem>> = repository.getData(path)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}