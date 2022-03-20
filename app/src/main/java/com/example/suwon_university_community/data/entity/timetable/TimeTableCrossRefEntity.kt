package com.example.suwon_university_community.data.entity.timetable

import androidx.room.Entity


@Entity(primaryKeys = ["tableId", "cellId"])
data class TimeTableCrossRefEntity(
    val tableId: Long,
    val cellId: Long
)