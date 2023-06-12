package com.android.shopmaker.data.repository

import com.android.shopmaker.data.service.network.DownloadDataSource
import javax.inject.Inject

class FileDownloadRepository @Inject constructor(
    private var downloadDataSource: DownloadDataSource
) {

    suspend fun downloadFile(fileUrl: String, fileName: String): String {
        return  downloadDataSource?.downloadFile(fileUrl, fileName) ?: ""
    }
}