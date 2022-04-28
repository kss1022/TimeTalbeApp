package net.suwon.plus.data.entity.timetable

import androidx.room.Entity


@Entity(primaryKeys = ["tableId", "cellId"])
data class TimeTableCrossRefEntity(
    val tableId: Long,
    val cellId: Long
)