package net.suwon.plus.ui.main.time_table

import androidx.annotation.StringRes
import net.suwon.plus.data.entity.timetable.TimeTableWithCell


sealed class TimeTableState{
    object Uninitialized : TimeTableState()

    object Loading : TimeTableState()

    object NoTable : TimeTableState()

    data class Success(
        val timeTableWithCell: TimeTableWithCell
    ) : TimeTableState()


    data class Error(
        @StringRes val massageId: Int
    ) : TimeTableState()
}