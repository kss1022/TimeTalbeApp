package com.example.suwon_university_community.ui.main.time_table.addTimeTable.tablelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


//todo 시간표를 삭제할시 -> 그 폴더를 삭제해줘야한다.
//  삭제된 시간표에 포함된 메모들을 기본 폴더로 옮겨준다.

class TimeTableListViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {


    val timeTableListStateLiveData =
        MutableLiveData<TimeTableListState>(TimeTableListState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        timeTableListStateLiveData.value = TimeTableListState.Loading

        val timeTableList =
            timeTableRepository.getTimeTableList().map { it.toModel() }
                .sortedByDescending { it.semester }.sortedByDescending { it.year }



        timeTableListStateLiveData.value =
            TimeTableListState.Success(timeTableList)
    }

    fun saveNewTimeTable(tableName: String, year: Int, semester: Int) = viewModelScope.launch {
        timeTableRepository.insertTimeTable(
            TimeTableEntity(
                tableName = tableName,
                year = year,
                semester = semester,
                isDefault = false
            )
        )
        fetchData()
    }


    fun editTimeTable(
        tableId: Long,
        tableName: String,
        year: Int,
        semester: Int,
        default: Boolean
    ) = viewModelScope.launch {
        timeTableRepository.updateTimeTable(
            TimeTableEntity(
                tableId =  tableId,
                tableName = tableName,
                year = year,
                semester = semester,
                isDefault = default
            )
        )
        fetchData()
    }


    fun changeMainTableId(timeTableId: Long) = viewModelScope.launch {
        preferenceManager.putMainTimeTableId(timeTableId)
    }

    fun deleteTimeTable(timeTableId: Long) = viewModelScope.launch {
        timeTableRepository.deleteTimeTableWithCell(timeTableId)
        fetchData()
    }
}
