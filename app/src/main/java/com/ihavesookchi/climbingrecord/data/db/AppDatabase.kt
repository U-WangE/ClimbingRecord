package com.ihavesookchi.climbingrecord.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ihavesookchi.climbingrecord.data.dao.ClimbingCenterDao
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse

@Database(entities = [SearchKeywordResponse.Document::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun climbingCenterDao(): ClimbingCenterDao
}