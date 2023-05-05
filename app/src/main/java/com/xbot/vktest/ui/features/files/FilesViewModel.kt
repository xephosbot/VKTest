package com.xbot.vktest.ui.features.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.vktest.data.FilesRepository
import com.xbot.vktest.model.File
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val repository: FilesRepository
) : ViewModel() {

    val filesList: StateFlow<List<File>> = repository.data
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
}