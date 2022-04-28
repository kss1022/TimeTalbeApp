package net.suwon.plus.data.entity.timetable

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlinx.parcelize.Parcelize


@Parcelize
data class TimeTableWithCell (
    @Embedded val timeTable : TimeTableEntity,
    @Relation(
        parentColumn = "tableId",
        entityColumn = "cellId",
        associateBy = Junction(TimeTableCrossRefEntity::class)
    )
    val timeTableCellList : List<TimeTableCellEntity>
) : Parcelable