package com.android.shopmaker.data.service.network.vo

import com.google.gson.annotations.SerializedName

data class EmergencyNoticeData(
    @SerializedName("message")
    var messsage:String = "",
    @SerializedName("title")
    var title:String = "",
    @SerializedName("description")
    var description:String = "",
    @SerializedName("createdAt")
    var createdAt:String = "",
    @SerializedName("isShow")
    var isShow:String = "",
)
