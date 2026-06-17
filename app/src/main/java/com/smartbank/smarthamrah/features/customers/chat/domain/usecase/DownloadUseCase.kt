package com.smartbank.smarthamrah.features.customers.chat.domain.usecase


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.smartbank.smarthamrah.features.customers.chat.domain.model.DownloadedFile
import com.smartbank.smarthamrah.features.customers.chat.domain.repository.ChatRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DownloadFileUseCase @Inject constructor(
    private val repository: ChatRepository,
    @ApplicationContext private val context: Context
) {

    suspend operator fun invoke(
        fileId: String
    ): DownloadedFile {

        val fileInfo = repository.getFileInfo(fileId)

        val body = repository.downloadFile(fileId)

        val file = saveFile(
            body = body,
            fileName = fileInfo.fileName
        )

        val uri = getFileUri(file)

        return DownloadedFile(
            uri = uri,
            fileName = fileInfo.fileName,
            mimeType = fileInfo.mimeType
        )
    }

    private fun saveFile(
        body: ResponseBody,
        fileName: String
    ): File {

        val directory =
            context.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS
            )!!

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, fileName)

        FileOutputStream(file).use { output ->
            body.byteStream().use { input ->
                input.copyTo(output)
            }
        }

        return file
    }

    private fun getFileUri(
        file: File
    ): Uri {

        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}