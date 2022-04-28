package net.suwon.plus.ui.main.time_table

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.data.entity.timetable.TimeTableWithCell
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.ui.base.BaseViewModel
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

        if (preferenceManager.getMainTimeTableId() == null) {
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


        when (val data = timeTableStateLiveData.value) {


            is TimeTableState.Success -> {
                timeTableStateLiveData.value = data.copy(
                    timeTableWithCell = TimeTableWithCell(
                        data.timeTableWithCell.timeTable,
                        data.timeTableWithCell.timeTableCellList.toMutableList()
                            .apply { add(timeTableCellEntity) })
                )
            }

            else -> Unit
        }
    }


    fun deleteTimeTableEntity(timeTableCellEntity: TimeTableCellEntity) = viewModelScope.launch {
        timeTableRepository.deleteTimeTableCellAtTable(
            mainTimeTable.timeTable.tableId,
            timeTableCellEntity.cellId
        )

        when (val data = timeTableStateLiveData.value) {
            is TimeTableState.Success -> {
                timeTableStateLiveData.value = data.copy(
                    timeTableWithCell = TimeTableWithCell(
                        data.timeTableWithCell.timeTable,
                        data.timeTableWithCell.timeTableCellList.toMutableList()
                            .apply { remove(timeTableCellEntity) })
                )
            }

            else -> Unit
        }
    }


    fun updateTimeTableEntity(timeTableCellEntity: TimeTableCellEntity) = viewModelScope.launch {
        timeTableRepository.updateTimeTableCell(timeTableCellEntity)


        when (val data = timeTableStateLiveData.value) {
            is TimeTableState.Success -> {

                val findEntity =
                    data.timeTableWithCell.timeTableCellList.find { it.cellId == timeTableCellEntity.cellId }

                timeTableStateLiveData.value = data.copy(
                    timeTableWithCell = TimeTableWithCell(
                        data.timeTableWithCell.timeTable,
                        data.timeTableWithCell.timeTableCellList.toMutableList()
                            .apply {
                                remove(findEntity)
                                add(timeTableCellEntity)
                            })
                )
            }

            else -> Unit
        }

    }


    companion object {
        private const val DEFAULT_TIME_TABLE_ID = 202201L
    }
}