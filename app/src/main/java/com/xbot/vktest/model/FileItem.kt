package com.xbot.vktest.model

enum class FileType {
    FOLDER,
    ARCHIVE,
    MS_WORD,
    MS_POWERPOINT,
    MS_EXCEL,
    PDF,
    RTF,
    AUDIO,
    GIF,
    IMAGE,
    VIDEO,
    TEXT,
    UNKNOWN
}

data class FileItem(
    val id: Long,
    val title: String,
    val date: String,
    val size: String,
    val path: String,
    val new: Boolean,
    val type: FileType
)
