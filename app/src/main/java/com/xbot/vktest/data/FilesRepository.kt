package com.xbot.vktest.data

import android.content.Context
import com.xbot.vktest.R
import com.xbot.vktest.model.FileItem
import com.xbot.vktest.model.FileType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
class FilesRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun getData(path: String): Flow<List<FileItem>> = flow {
        val allDirectories = getFiles(path)
            .filter { !it.isHidden }
            .sortedBy { it.name }
        val foldersList = allDirectories.filter { it.isDirectory }
        val filesList = allDirectories.filter { it.isFile }

        val totalList = coroutineScope {
            foldersList.plus(filesList).map {
                async {
                    FileItem(
                        id = it.hashCode().toLong(),
                        title = it.name,
                        date = it.lastModified().toDate("dd MMM yyyy, HH:ss"),
                        size = if (it.isDirectory) getElementsCount(it) else it.length().toSize("0.00"),
                        path = it.path,
                        type = if (it.isDirectory) FileType.FOLDER else it.name.toFileType()
                    )
                }
            }
        }
        emit(totalList.awaitAll())
    }.flowOn(dispatcher)

    private fun getFiles(path: String): List<File> {
        val root = File(path)
        return root.listFiles()?.toList() ?: emptyList()
    }

    private fun getElementsCount(file: File): String {
        val count = file.listFiles()?.size ?: 0
        return context.resources.getQuantityString(R.plurals.number_of_folder_items, count, count)
    }

    private fun String.toFileType(): FileType {
        val lowerCaseName = this.lowercase()
        return when {
            lowerCaseName.endsWith(".zip") || lowerCaseName.endsWith(".rar") -> FileType.ARCHIVE
            lowerCaseName.endsWith(".doc") || lowerCaseName.endsWith(".docx") -> FileType.MS_WORD
            lowerCaseName.endsWith(".doc") || lowerCaseName.endsWith(".docx") -> FileType.MS_WORD
            lowerCaseName.endsWith(".ppt") || lowerCaseName.endsWith(".pptx") -> FileType.MS_POWERPOINT
            lowerCaseName.endsWith(".xls") || lowerCaseName.endsWith(".xlsx") -> FileType.MS_EXCEL
            lowerCaseName.endsWith(".pdf") -> FileType.PDF
            lowerCaseName.endsWith(".rtf") -> FileType.RTF
            lowerCaseName.endsWith(".mp3") || lowerCaseName.endsWith(".wav") || lowerCaseName.endsWith(".ogg") || lowerCaseName.endsWith(".m4a") -> FileType.AUDIO
            lowerCaseName.endsWith(".gif") -> FileType.GIF
            lowerCaseName.endsWith(".jpg") || lowerCaseName.endsWith(".jpeg") || lowerCaseName.endsWith(".png") || lowerCaseName.endsWith(".webp") || lowerCaseName.endsWith(".heif") -> FileType.IMAGE
            lowerCaseName.endsWith(".mp4") || lowerCaseName.endsWith(".avi") || lowerCaseName.endsWith("mov") || lowerCaseName.endsWith(".3gp") || lowerCaseName.endsWith(".flv") || lowerCaseName.endsWith(".mpg") -> FileType.VIDEO
            lowerCaseName.endsWith(".txt") -> FileType.TEXT
            else -> FileType.UNKNOWN
        }
    }

    private fun Long.toDate(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
    }

    private fun Long.toSize(pattern: String): String {
        val df = DecimalFormat(pattern)

        val sizeKb = 1024.0f
        val sizeMb = sizeKb * sizeKb
        val sizeGb = sizeMb * sizeKb
        val sizeTb = sizeGb * sizeKb

        return when {
            (this < sizeKb) -> "$this B"
            (this < sizeMb) -> "${df.format(this / sizeKb)} KB"
            (this < sizeGb) -> "${df.format(this / sizeMb)} MB"
            (this < sizeTb) -> "${df.format(this / sizeGb)} GB"
            else -> "${df.format(this / sizeTb)} TB"
        }
    }
}