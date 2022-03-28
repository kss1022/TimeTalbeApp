package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist

import androidx.annotation.StringRes
import com.example.suwon_university_community.model.LectureModel

sealed class LectureListState {
    object Uninitialized : LectureListState()
    object Loading : LectureListState()
    object Success : LectureListState()

    data class Added(
        val addedMessage : String
    ) : LectureListState()
    data class NotAdded(
        val model : LectureModel
    ) : LectureListState()

    data class Error(
        @StringRes val massageId: Int
    ) : LectureListState()

}