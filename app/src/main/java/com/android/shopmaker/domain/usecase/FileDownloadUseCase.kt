package com.android.shopmaker.domain.usecase

import com.android.shopmaker.data.repository.FileDownloadRepository
import com.android.shopmaker.domain.entity.FileDownloadEntity
import javax.inject.Inject

class FileDownloadUseCase @Inject constructor(
    private val downloadRepository: FileDownloadRepository
) {
    suspend fun downloadFile(fileUrl: String, fileName: String) : FileDownloadEntity {
        var filePath =  downloadRepository.downloadFile(fileUrl, fileName)
        return FileDownloadEntity(filePath, "")
    }
}