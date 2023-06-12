package com.android.shopmaker.data.service.network

import com.android.shopmaker.data.service.network.vo.BannerData
import javax.inject.Inject

interface RemoteDataSource {
    /// 배너 정보 취득
    suspend fun banner(code: String) : Response<List<BannerData>>
}

class RemoteDataSourceImpl @Inject constructor(
    private val remoteServiceAPI: RemoteServiceAPI
): RemoteDataSource {
    override suspend fun banner(code: String): Response<List<BannerData>> {
        return remoteServiceAPI.banner(code)
    }
}