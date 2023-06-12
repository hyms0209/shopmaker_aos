package com.android.shopmaker.data.service.network.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BannerData(
    @Expose @SerializedName("bannerSeq") val bannerSeq: String,  //배너순번
    @Expose @SerializedName("atchflId") val atchflId: String, //이미지경로
    @Expose @SerializedName("urlCcd") val urlCcd: String?,   //링크구분
    @Expose @SerializedName("nttUrl") val nttUrl: String?,     //링크URL
    @Expose @SerializedName("frstRdtt") val frstRdtt: String, //등록일
    @Expose @SerializedName("nttTit") val nttTit: String, //제목
    @Expose @SerializedName("linkCcd") val linkCcd: String?, //내부 / 새창
)
