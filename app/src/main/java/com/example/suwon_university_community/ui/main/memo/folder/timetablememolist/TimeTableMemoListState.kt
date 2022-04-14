package com.example.suwon_university_community.ui.main.memo.folder.timetablememolist

import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.model.MemoModel

sealed class TimeTableMemoListState {
    object Uninitialized : TimeTableMemoListState()

    object Loading : TimeTableMemoListState()

    data class Success(
        val timeTableWithCell: TimeTableWithCell,
        val memoList: List<MemoModel>
    ) : TimeTableMemoListState()


    data class NoTimeTable(
        val timeTableList: List<TimeTableEntity>
    ) : TimeTableMemoListState()

}