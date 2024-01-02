package com.ihavesookchi.climbingrecord.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse

@Dao
interface ClimbingCenterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClimbingCenter(climbingCenter: SearchKeywordResponse.Document)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllClimbingCenter(climbingCenters: List<SearchKeywordResponse.Document>)

    @Query("SELECT * FROM climbing_centers")
    suspend fun getAllClimbingCenters(): List<SearchKeywordResponse.Document>
}