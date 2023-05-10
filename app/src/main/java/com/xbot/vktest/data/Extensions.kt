package com.xbot.vktest.data

import com.xbot.vktest.model.FileType
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TreeMap

inline fun <K, V, R> Map<out K, V>.orderedFlatMap(transform: (Map.Entry<K, V>) -> Iterable<R>): List<R> {
    return TreeMap(this).flatMapTo(ArrayList(), transform)
}

fun File.format(): String {
    return name.split(".").takeIf { it.size > 1 }?.last() ?: ""
}

fun String.toFileType(): FileType {
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

fun Long.toDate(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}

fun Long.toSize(pattern: String): String {
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