package com.android.shopmaker.data.service.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface FileDownloadAPI {

    /** 파일 다운로드 **/
    @GET
    @Streaming
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}