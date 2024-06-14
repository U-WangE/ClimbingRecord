package com.ihavesookchi.climbingrecord.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordsDataResponse(
    @SerializedName("recordList")
    val recordList: List<Record> = listOf()
): Parcelable {

    @Parcelize
    data class Record(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("recordImages")
        val recordImages: List<String> = listOf(),
        @SerializedName("achievementList")
        val achievementDataList: List<AchievementData> = listOf(),
        @SerializedName("achievementDate")
        val achievementDate: Long = 0L
    ): Parcelable {
        @Parcelize
        data class AchievementData(
            @SerializedName("achievementQuantity")
            var achievementQuantity: Int = 0,
            @SerializedName("achievementColorRGB")
            var achievementColorRGB: String = ""
        ) : Parcelable
    }
}