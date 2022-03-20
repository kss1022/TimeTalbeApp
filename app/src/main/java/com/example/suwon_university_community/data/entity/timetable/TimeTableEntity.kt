package com.example.suwon_university_community.data.entity.timetable

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TimeTableEntity (
    @PrimaryKey val tableId : Long,
    val year : String,
    val semester : String
)