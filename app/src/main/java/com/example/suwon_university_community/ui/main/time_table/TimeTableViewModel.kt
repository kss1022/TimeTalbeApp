package com.example.suwon_university_community.ui.main.time_table

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject



class TimeTableViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val timeTableStateLiveData = MutableLiveData<TimeTableState>(TimeTableState.Uninitialized)

    lateinit var mainTimeTable: TimeTableWithCell

    override fun fetchData(): Job = viewModelScope.launch {
        timeTableStateLiveData.value = TimeTableState.Loading

        val mainTimeTableId = preferenceManager.getMainTimeTableId()

        mainTimeTableId?.let {
            mainTimeTable =
                timeTableRepository.getTimeTableWithCell(preferenceManager.getMainTimeTableId()!!)
            timeTableStateLiveData.value = TimeTableState.Success(mainTimeTable)
        } ?: kotlin.run {
            timeTableStateLiveData.value = TimeTableState.NoTable
        }

    }


    fun saveNewTimeTable(tableName: String, year: Int, semester: Int) = viewModelScope.launch {

        if(preferenceManager.getMainTimeTableId() == null){
            timeTableRepository.insertTimeTable(
                TimeTableEntity(
                    tableId = DEFAULT_TIME_TABLE_ID,
                    tableName = tableName,
                    year = year,
                    semester = semester,
                    isDefault = true
                )
            )

            preferenceManager.putMainTimeTableId(DEFAULT_TIME_TABLE_ID)
            fetchData()
        }
    }


    fun addTimeTableEntity(timeTableCellEntity: TimeTableCellEntity) = viewModelScope.launch {
        timeTableRepository.insertTimeTableCellWithTable(
            mainTimeTable.timeTable.tableId,
            timeTableCellEntity
        )
        fetchData()
    }


    fun deleteTImeTableEntity(cellId: Long) = viewModelScope.launch {
        timeTableRepository.deleteTimeTableCellAtTable(mainTimeTable.timeTable.tableId, cellId)
        fetchData()
    }

    fun updateTimeTableEntity(timeTableCellEntity: TimeTableCellEntity) = viewModelScope.launch {
        timeTableRepository.updateTimeTableCell(timeTableCellEntity)
        fetchData()
    }



    companion object {
        private const val DEFAULT_TIME_TABLE_ID = 202201L
    }
}