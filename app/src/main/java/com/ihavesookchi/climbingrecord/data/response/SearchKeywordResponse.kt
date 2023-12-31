package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class SearchKeywordResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("documents")
    val documents: List<Document>
) {
    data class Meta(
        @SerializedName("total_count")
        val totalCount: Int,
        @SerializedName("same_name")
        val sameName: SameName
    ) {
        data class SameName(
            @SerializedName("keyword")
            val keyword: String
        )
    }

    data class Document(
        @SerializedName("id")
        val id: String,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("address_name")
        val addressName: String,
        @SerializedName("road_address_name")
        val roadAddressName: String,
        @SerializedName("x")  // longitude
        val longitude: String,
        @SerializedName("y")  // latitude
        val latitude: String
    )
}
