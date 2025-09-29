package com.uwange.climbingrecord.domain.model

data class ClimbingRecord(
    val id: String,
    val date: String,
    val location: String,
    val difficulty: String,
    val notes: String? = null
)
