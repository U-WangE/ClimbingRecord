package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class SearchKeyWordResponse(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("document")
    val document: Document
) {
    data class Meta(
        @SerializedName("total_count")
        val totalCount: Int
    )

    data class Document(
        @SerializedName("id")
        val id: String,
        @SerializedName("place_name")
        val placeName: String,
        @SerializedName("address_name")
        val addressName: String,
        @SerializedName("road_address_name")
        val roadAddressName: String,
        @SerializedName("x")
        val x: String,
        @SerializedName("y")
        val y: String
    )
}
