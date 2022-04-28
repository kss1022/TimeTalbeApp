package net.suwon.plus.ui.main.time_table.addTimeTable.tablelist

import net.suwon.plus.model.TimeTableModel


sealed class TimeTableListState {
    object Uninitialized : TimeTableListState()

    object Loading : TimeTableListState()

    data class Success(
        val timeTableModelList: List<TimeTableModel>
    ) : TimeTableListState()
}