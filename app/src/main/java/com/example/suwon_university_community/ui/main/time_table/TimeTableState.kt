package com.example.suwon_university_community.ui.main.time_table

import androidx.annotation.StringRes
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell


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