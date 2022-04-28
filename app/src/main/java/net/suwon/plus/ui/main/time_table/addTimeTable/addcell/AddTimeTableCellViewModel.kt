package net.suwon.plus.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.suwon.plus.R
import net.suwon.plus.data.entity.timetable.DayOfTheWeek
import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableLocationAndTime
import net.suwon.plus.data.repository.timetable.TimeTableRepository
import net.suwon.plus.model.LectureModel
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.ui.main.time_table.TableColorCategory
import javax.inject.Inject

class AddTimeTableCellViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
) : BaseViewModel() {


    val deleteState = MutableLiveData<Boolean>()
    private val currentAddedIdList = mutableListOf<Long>()

    fun addLecture(
        timeTableId: Long,
        lectureModel: LectureModel,
        locationAndTimeList: List<TimeTableLocationAndTime>
    ) = viewModelScope.launch {
        val timetableWithCell = timeTableRepository.getTimeTableWithCell(timeTableId)


        val cellColor : Int

        if(locationAndTimeList.isNullOrEmpty() || ( locationAndTimeList.size == 1) && locationAndTimeList.first().day== DayOfTheWeek.DEFAULT  ){
            cellColor = R.color.colorPrimary
        }else{
            val colorList = TableColorCategory.values().toMutableList()

            timetableWithCell.timeTableCellList.forEach { newCell->
                if( newCell.locationAndTimeList.isNullOrEmpty().not() ){
                    colorList.find { it.colorId  == newCell.cellColor }?.let {
                        colorList.remove(colorList.find { it.colorId  == newCell.cellColor })
                    }
                }
            }

            cellColor = if(colorList.isEmpty()){
                TableColorCategory.values().random().colorId
            }else{
                //cellColor = colorList[Random.nextInt(colorList.size)].colorId
                colorList.first().colorId
            }
        }


        val currentTime= System.currentTimeMillis()

        currentAddedIdList.add(currentTime)

        timeTableRepository.insertTimeTableCellWithTable(
            timeTableId, TimeTableCellEntity(
                cellId = currentTime,
                name = lectureModel.name ?: "",
                distinguish = lectureModel.distinguish ?: "",
                point = lectureModel.point ?: 0f,
                locationAndTimeList = locationAndTimeList,
                professorName = lectureModel.professorName ?: "",
                cellColor = cellColor
            )
        )
    }

    fun deleteLecture( tableId: Long) = viewModelScope.launch{
        currentAddedIdList.forEach { id->
            timeTableRepository.deleteTimeTableCellAtTable(tableId ,id)
        }

        deleteState.value = true
    }
}