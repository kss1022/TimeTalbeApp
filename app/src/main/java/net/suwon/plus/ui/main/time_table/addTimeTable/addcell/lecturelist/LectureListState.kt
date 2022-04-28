package net.suwon.plus.ui.main.time_table.addTimeTable.addcell.lecturelist

import androidx.annotation.StringRes
import net.suwon.plus.model.LectureModel

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