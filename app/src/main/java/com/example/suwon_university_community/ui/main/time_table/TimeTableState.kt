package com.example.suwon_university_community.ui.main.time_table

import androidx.annotation.StringRes


sealed class TimeTableState{
    object Uninitialized : TimeTableState()
    object Loading : TimeTableState()
    object Success : TimeTableState()
    object NoTable : TimeTableState()

    data class Error(
        @StringRes val massageId: Int
    ) : TimeTableState()
}