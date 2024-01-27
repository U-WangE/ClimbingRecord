package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("nickname")
    var nickname: String = "Unknown",
    @SerializedName("profileImage")
    var profileImage: String = "",
    @SerializedName("instagramUserName")
    var instagramUserName: String = ""
)
