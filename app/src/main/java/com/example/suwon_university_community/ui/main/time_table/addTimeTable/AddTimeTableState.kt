package com.example.suwon_university_community.ui.main.time_table.addTimeTable

import androidx.annotation.StringRes

sealed class AddTimeTableState {
    object Uninitialized : AddTimeTableState()
    object Loading : AddTimeTableState()
    object Success : AddTimeTableState()
    data class Error(
        @StringRes val massageId: Int
    ) : AddTimeTableState()
}