package com.ihavesookchi.climbingrecord.data.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SearchKeywordResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("document")
    val document: List<Document>
) {
    data class Meta(
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("pageable_count")
        val pageableCount: Int,
        @SerializedName("last_update_date")
        val lastUpdateDate: Long,
        @SerializedName("same_name")
        val sameName: SameName
    ) {
        data class SameName(
            @SerializedName("keyword")
            val keyword: String
        )
    }

    @Entity(tableName = "climbing_centers")
    data class Document(
        @PrimaryKey
        @SerializedName("id")
        val id: String,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("address_name")
        val addressName: String,
        @SerializedName("road_address_name")
        val roadAddressName: String,
        @SerializedName("x")  // longitude
        val x: String,
        @SerializedName("y")  // latitude
        val y: String
    )
}
