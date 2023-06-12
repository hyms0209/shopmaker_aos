package com.android.shopmaker.data.service.network.vo

import com.google.gson.annotations.SerializedName

data class MomentRegistResult(
    @SerializedName("id")
    var id:String = "",
    @SerializedName("createdAt")
    var createdAt:String = "",
)
