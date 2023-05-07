package com.xbot.vktest.ui.extensions

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.xbot.vktest.R
import com.xbot.vktest.model.FileItem
import com.xbot.vktest.model.FileType
import java.io.File

fun FileType.toImageResource(): Int {
    return when(this) {
        FileType.FOLDER -> R.drawable.baseline_folder_40
        FileType.ARCHIVE -> R.drawable.baseline_archive_40
        FileType.MS_WORD -> R.drawable.baseline_word_40
        FileType.MS_POWERPOINT -> R.drawable.baseline_powerpoint_40
        FileType.MS_EXCEL -> R.drawable.baseline_excel_40
        FileType.PDF -> R.drawable.baseline_pdf_40
        FileType.RTF -> R.drawable.baseline_rtf_40
        FileType.AUDIO -> R.drawable.baseline_audio_40
        FileType.GIF -> R.drawable.baseline_gif_40
        FileType.IMAGE -> R.drawable.baseline_image_40
        FileType.VIDEO -> R.drawable.baseline_video_40
        FileType.TEXT -> R.drawable.baseline_text_40
        else -> R.drawable.baseline_file_40
    }
}

fun FileItem.toIntent(context: Context): Intent {
    val intent = Intent(Intent.ACTION_VIEW)
    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        File(path)
    )

    intent.setDataAndType(uri, type.toFileContentType())
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return intent
}

private fun FileType.toFileContentType(): String {
    return when(this) {
        FileType.ARCHIVE -> "application/zip"
        FileType.MS_WORD -> "application/msword"
        FileType.MS_POWERPOINT -> "application/vnd.ms-powerpoint"
        FileType.MS_EXCEL -> "application/vnd.ms-excel"
        FileType.PDF -> "application/pdf"
        FileType.RTF -> "application/rtf"
        FileType.AUDIO -> "audio/*"
        FileType.GIF -> "image/gif"
        FileType.IMAGE -> "image/*"
        FileType.VIDEO -> "video/*"
        FileType.TEXT -> "text/plain"
        else -> "*/*"
    }
}