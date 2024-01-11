package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class SearchKeywordResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("documents")
    val documents: List<Document>
) {
    data class Meta(
        @SerializedName("totalCount")
        val totalCount: Int,
        @SerializedName("sameName")
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
        @SerializedName("placeName")
        val placeName: String,
        @SerializedName("addressName")
        val addressName: String,
        @SerializedName("roadAddressName")
        val roadAddressName: String,
        @SerializedName("x")  // longitude
        val longitude: String,
        @SerializedName("y")  // latitude
        val latitude: String
    )
}
