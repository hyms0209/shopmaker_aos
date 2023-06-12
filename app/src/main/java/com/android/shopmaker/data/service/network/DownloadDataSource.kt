package com.android.shopmaker.data.service.network

import android.util.Log
import com.android.shopmaker.AppApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


interface DownloadDataSource {
    suspend fun downloadFile(fileUrl: String, fileName: String): String
}

class DownloadDataSourceImpl @Inject constructor(
    private val fileDownloadApi: FileDownloadAPI
) : DownloadDataSource {

    override suspend fun downloadFile(fileUrl: String, fileName: String): String {
        var fileFullpath = ""
        withContext(Dispatchers.IO) {
            val response = fileDownloadApi.downloadFile(fileUrl).execute()
            if (response.isSuccessful) {
                val responseBody: ResponseBody? = response.body()
                if (responseBody != null) {
                    try {
                        val file = File(AppApplication.ctx?.filesDir, fileName)
                        val inputStream = responseBody.byteStream()
                        val outputStream = FileOutputStream(file)

                        val buffer = ByteArray(4096)
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }

                        outputStream.flush()
                        outputStream.close()
                        inputStream.close()
                        fileFullpath = file.absolutePath
                    } catch (e: Exception) {
                        Log.e("FileDownLoad", "파일 저장 중 예외 발생: ${e.message}")
                        Log.d("FileDownLoad", "===========> FileDownload Fail!!!!")
                    }
                }
            } else {
                // 다운로드 실패 처리
            }
        }
        return fileFullpath
    }
}