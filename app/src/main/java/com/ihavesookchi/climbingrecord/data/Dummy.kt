package com.ihavesookchi.climbingrecord.data

import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse

object Dummy {
    fun getSearchKeyWordResponse(): SearchKeywordResponse {
        return SearchKeywordResponse(
            meta = SearchKeywordResponse.Meta(
                totalCount = 5,
                sameName = SearchKeywordResponse.Meta.SameName(
                    keyword = "클라이밍"
                )
            ),
            documents = listOf(
                SearchKeywordResponse.Document(
                    id = "807578478",
                    placeName = "더클라임 강남점",
                    addressName = "서울 강남구 역삼동 823-14",
                    roadAddressName = "서울 강남구 테헤란로8길 21",
                    x = "127.031992024802",
                    y = "37.497588868698"
                ),
                SearchKeywordResponse.Document(
                    id = "662485726",
                    placeName = "클라이밍파크 성수점",
                    addressName = "서울 성동구 성수동2가 273-34",
                    roadAddressName = "서울 성동구 연무장13길 7",
                    x = "127.058089608702",
                    y = "37.5423101113247"
                ),
                SearchKeywordResponse.Document(
                    id = "1786078320",
                    placeName = "더플라스틱클라이밍 염창점",
                    addressName = "서울 강서구 염창동 262-1",
                    roadAddressName = "서울 강서구 공항대로81길 27",
                    x = "126.876191936854",
                    y = "37.5485025153864"
                ),
                SearchKeywordResponse.Document(
                    id = "1491561655",
                    placeName = "서울시산악문화체험센터",
                    addressName = "서울 마포구 상암동 481-231",
                    roadAddressName = "서울 마포구 하늘공원로 112",
                    x = "126.87992666095334",
                    y = "37.569441929043634"
                ),
                SearchKeywordResponse.Document(
                    id = "1330937676",
                    placeName = "피커스 종로점",
                    addressName = "서울 종로구 돈의동 137",
                    roadAddressName = "서울 종로구 돈화문로5가길 1",
                    x = "126.991397046749",
                    y = "37.57091093329"
                )
            )
        )
    }
}