package com.android.shopmaker.data.service.network

import com.android.shopmaker.data.service.network.vo.BannerData
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteServiceAPI {
    /**
     * 배너 조회
     */
    @GET("/api/sm/na/banner")
    suspend fun banner(
        @Query("ntceLocCcd") ntceLocCcd: String,
    ) : Response<List<BannerData>>
}

