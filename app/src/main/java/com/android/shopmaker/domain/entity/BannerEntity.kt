package com.android.shopmaker.domain.entity

data class BannerEntity(
    val bannerIdx: Int,
    val title: String,
    val imgPath: String,
    val imgPathCf: String,
    val linkType: String?,
    val linkUrl: String?,
    val linkForm: String?,
    val regDt: String,  // yyyy-MM-dd HH:mm:ss
)