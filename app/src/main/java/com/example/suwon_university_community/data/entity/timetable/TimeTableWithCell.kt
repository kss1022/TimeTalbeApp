package com.example.suwon_university_community.data.entity.timetable

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TimeTableWithCell (
    @Embedded val timeTable : TimeTableEntity,
    @Relation(
        parentColumn = "tableId",
        entityColumn = "cellId",
        associateBy = Junction(TimeTableCrossRefEntity::class)
    )
    val timeTableCell : List<TimeTableCellEntity>
)