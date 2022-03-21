package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.annotation.StringRes

sealed class AddTimeTableCellState {
    object Uninitialized : AddTimeTableCellState()
    object Loading : AddTimeTableCellState()
    object Success : AddTimeTableCellState()
    data class Error(
        @StringRes val massageId: Int
    ) : AddTimeTableCellState()
}