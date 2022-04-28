package net.suwon.plus.ui.main.memo.folder.timetablememolist

import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.data.entity.timetable.TimeTableWithCell
import net.suwon.plus.model.MemoModel

sealed class TimeTableMemoListState {
    object Uninitialized : TimeTableMemoListState()

    object Loading : TimeTableMemoListState()

    data class Success(
        val timeTableWithCell: TimeTableWithCell,
        val memoList: List<MemoModel>
    ) : TimeTableMemoListState()


    data class EditMemo(
        val memoList: List<MemoModel>
    ): TimeTableMemoListState()

    data class NoTimeTable(
        val timeTableList: List<TimeTableEntity>
    ) : TimeTableMemoListState()
}