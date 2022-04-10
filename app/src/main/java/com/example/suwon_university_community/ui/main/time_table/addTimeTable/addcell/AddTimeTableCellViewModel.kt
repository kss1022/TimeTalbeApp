package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.timetable.DayOfTheWeek
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.ui.main.time_table.TableColorCategory
import com.example.suwon_university_community.util.provider.ResourceProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTimeTableCellViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

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

            if(colorList.isEmpty()){
                cellColor = TableColorCategory.values().random().colorId
            }else{
                cellColor = colorList.first().colorId
            }
        }



        timeTableRepository.insertTimeTableCellWithTable(
            timeTableId, TimeTableCellEntity(
                cellId = System.currentTimeMillis(),
                name = lectureModel.name ?: "",
                distinguish = lectureModel.distinguish ?: "",
                point = lectureModel.point ?: 0f,
                locationAndTimeList = locationAndTimeList,
                professorName = lectureModel.professorName ?: "",
                cellColor = cellColor
            )
        )
    }
}