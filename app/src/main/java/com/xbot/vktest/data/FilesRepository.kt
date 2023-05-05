package com.xbot.vktest.data

import com.xbot.vktest.model.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FilesRepository {

    private val list = listOf(
        File(
            id = 0,
            title = "Печь.gif",
            date = "21 апр. 23:04",
            size = "53,79 КБ"
        ),
        File(
            id = 1,
            title = "resume.pdf",
            date = "15 апр. 12:03",
            size = "38,16 КБ"
        ),
        File(
            id = 2,
            title = "32395393_huge.jpg",
            date = "15 апр. 02:20",
            size = "78,68 КБ"
        ),
        File(
            id = 3,
            title = "catalog-debug.apk",
            date = "13 апр. 01:18",
            size = "14,48 МБ"
        ),
        File(
            id = 4,
            title = "Фото178.jpg",
            date = "8 апр. 23:26",
            size = "303 КБ"
        ),
        File(
            id = 5,
            title = "history.docx",
            date = "6 апр. 11:50",
            size = "14,77 КБ"
        )
    )

    val data: Flow<List<File>> = flow {
        emit(list.shuffled() + list.shuffled() + list.shuffled() + list.shuffled())
    }
}