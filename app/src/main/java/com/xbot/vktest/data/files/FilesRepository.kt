package com.xbot.vktest.data.files

import android.content.Context
import com.xbot.vktest.R
import com.xbot.vktest.data.format
import com.xbot.vktest.data.orderedFlatMap
import com.xbot.vktest.data.toDate
import com.xbot.vktest.data.toFileType
import com.xbot.vktest.data.toSize
import com.xbot.vktest.model.FileEntity
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
import java.util.TreeSet
import javax.inject.Singleton

@Singleton
class FilesRepository(
    private val fileDao: FileDao,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val cache: TreeSet<Int> = TreeSet()

    fun getData(path: String, sortKey: Int): Flow<List<FileItem>> = flow {
        val rawFiles = getFiles(path).filter { !it.isHidden }
        val groupedFiles = rawFiles.groupBy { it.isFile }

        val fileList = coroutineScope {
            groupedFiles.orderedFlatMap { (_, sublist) ->
                sublist.run {
                    when(sortKey) {
                        SORT_BY_NAME_ACS -> sortedBy { it.name }
                        SORT_BY_NAME_DESC -> sortedByDescending { it.name }
                        SORT_BY_SIZE_ACS -> sortedBy { it.length() }
                        SORT_BY_SIZE_DESC -> sortedByDescending { it.length() }
                        SORT_BY_DATE_ACS -> sortedBy { it.lastModified() }
                        SORT_BY_DATE_DESC -> sortedByDescending { it.lastModified() }
                        SORT_BY_FORMAT_ACS -> sortedBy { it.format() }
                        SORT_BY_FORMAT_DESC -> sortedByDescending { it.format() }
                        else -> sortedBy { it.name }
                    }
                }
            }.map {
                async {
                    FileItem(
                        id = it.hashCode().toLong(),
                        title = it.name,
                        date = it.lastModified().toDate("dd MMM yyyy, HH:ss"),
                        size = if (it.isDirectory) getElementsCount(it) else it.length().toSize("0.00"),
                        path = it.path,
                        new = !cache.contains(it.hashCode()),
                        type = if (it.isDirectory) FileType.FOLDER else it.name.toFileType()
                    )
                }
            }
        }
        emit(fileList.awaitAll())
    }.flowOn(dispatcher)

    suspend fun prepareDirectories() {
        cache.addAll(fileDao.getAll().map { it.uid })
    }

    suspend fun saveDirectories(path: String) {
        val files = getFiles(path)
        val folders = files.filter { it.isDirectory }
        fileDao.insertAll(files.map { FileEntity(it.hashCode(), it.path) })
        folders.forEach { saveDirectories(it.path) }
    }

    private fun getFiles(path: String): List<File> {
        val root = File(path)
        return root.listFiles()?.toList() ?: emptyList()
    }

    private fun getElementsCount(file: File): String {
        val count = file.listFiles()?.size ?: 0
        return context.resources.getQuantityString(R.plurals.number_of_folder_items, count, count)
    }

    companion object {
        private const val SORT_BY_NAME_ACS = 0
        private const val SORT_BY_NAME_DESC = 1
        private const val SORT_BY_SIZE_ACS = 2
        private const val SORT_BY_SIZE_DESC = 3
        private const val SORT_BY_DATE_ACS = 4
        private const val SORT_BY_DATE_DESC = 5
        private const val SORT_BY_FORMAT_ACS = 6
        private const val SORT_BY_FORMAT_DESC = 7
    }
}