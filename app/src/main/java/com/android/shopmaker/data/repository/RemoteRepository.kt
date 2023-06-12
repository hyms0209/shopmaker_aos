package com.android.shopmaker.data.repository

import com.android.shopmaker.data.service.network.RemoteDataSource
import com.android.shopmaker.data.service.network.Resource
import com.android.shopmaker.domain.entity.BannerEntity
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
)  {
    suspend fun banner(code: String) : Resource<List<BannerEntity>> {
        return remoteDataSource.banner(code).run {
            matcherNullable { listData ->
                listData?.map { bannerData ->
                    BannerEntity(
                        bannerIdx = bannerData.bannerSeq.toInt(),
                        title = bannerData.nttTit,
                        imgPath = bannerData.atchflId,
                        imgPathCf = bannerData.atchflId,
                        linkType = bannerData.urlCcd,
                        linkUrl = bannerData.nttUrl,
                        linkForm = bannerData.linkCcd,
                        regDt = bannerData.frstRdtt,
                    )
                }
            }
        }
    }
}