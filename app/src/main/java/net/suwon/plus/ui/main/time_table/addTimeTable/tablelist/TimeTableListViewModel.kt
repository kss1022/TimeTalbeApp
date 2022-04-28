package net.suwon.plus.ui.main.time_table.addTimeTable.tablelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.model.TimeTableModel
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject


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

        val tableEntity =    TimeTableEntity(
            tableName = tableName,
            year = year,
            semester = semester,
            isDefault = false
        )

        timeTableRepository.insertTimeTable(tableEntity)
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

    fun deleteTimeTable(timeTableModel: TimeTableModel) = viewModelScope.launch {
        timeTableRepository.deleteTimeTableWithCell(timeTableModel.id)

        when(val data = timeTableListStateLiveData.value){
            is TimeTableListState.Success->{
                timeTableListStateLiveData.value = data.copy(
                    data.timeTableModelList.toMutableList().apply {
                        remove(timeTableModel)
                    }
                )
            }

            else->Unit
        }
    }
}
