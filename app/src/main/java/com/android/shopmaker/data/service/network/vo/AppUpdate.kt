package com.android.shopmaker.data.service.network.vo

import com.google.gson.annotations.SerializedName

data class AppUpdateData(
    @SerializedName("os")
    var os:String = "",
    @SerializedName("version")
    var version:String = "",
    @SerializedName("status")
    var status:String = "",
    var title:String = "",
    var message:String = ""
)
