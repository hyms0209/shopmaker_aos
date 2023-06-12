package com.android.shopmaker.data.service.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class HttpIntercepter: Interceptor {
    private val EMPTY_JSON = "{}"

    /**
     * http status code가 200이 아닌경우
     * http status의 처리 프로세스가 별도로 없으므로
     * 일단 status를 body에 저장하고
     * Repository 혹은 Usecase 등에서 처리
     */

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        try {
            val response = chain.proceed(originalRequest)
            if (response.code == RemoteError.HTTP_STATUS_OK) {
                return response
            } else {
                return response
                    .extractResponseJson()
                    .put("status", response.code)
                    .toString().let { json ->
                        response.newBuilder()
                            .code(RemoteError.HTTP_STATUS_OK)
                            .body(json.toResponseBody("application/json".toMediaType()))
                            .build()
                    }
            }
        } catch (e: Exception) {
            // Interceptor단 에러
            val errJson = JSONObject().apply {
                put("status", RemoteError.HTTP_STATUS_OK)
                put("errCode", RemoteError.ERR_CODE_INTERNAL)

                e.printStackTrace()
                put("errMsg", "${e::class.simpleName}: ${e.message}")

            }
            return Response.Builder()
                .code(RemoteError.HTTP_STATUS_OK)
                .request(originalRequest)
                .message(e.message ?: "")
                .body(errJson.toString().toResponseBody("application/json".toMediaType()))
                .protocol(Protocol.HTTP_1_1)
                .build()
        }
    }

    @Throws(Exception::class)
    private fun Response.extractResponseJson(): JSONObject {
        val jsonString = this.body?.string() ?: EMPTY_JSON
        return JSONObject(jsonString)
    }
}