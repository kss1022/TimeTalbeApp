package com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist

import com.example.suwon_university_community.model.TimeTableModel


sealed class TimeTableListState {
    object Uninitialized : TimeTableListState()

    object Loading : TimeTableListState()

    data class Success(
        val timeTableModelList: List<TimeTableModel>
    ) : TimeTableListState()
}