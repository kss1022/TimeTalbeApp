package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.ui.main.time_table.TableColorCategory
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTimeTableCellViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository
) : BaseViewModel() {

    fun addLecture(
        timeTableId: Long,
        lectureModel: LectureModel,
        locationAndTimeList: List<TimeTableLocationAndTime>
    ) = viewModelScope.launch {
        val timetableWithCell = timeTableRepository.getTimeTableWithCell(timeTableId)


        var colorCount = timetableWithCell.timeTableCellList.size
        if (colorCount >= TableColorCategory.values().size) {
            colorCount -= TableColorCategory.values().size
        }

        timeTableRepository.insertTimeTableCellWithTable(
            timeTableId, TimeTableCellEntity(
                cellId = System.currentTimeMillis(),
                name = lectureModel.name ?: "",
                distinguish = lectureModel.distinguish ?: "",
                point = lectureModel.point ?: 0f,
                locationAndTimeList = locationAndTimeList,
                professorName = lectureModel.professorName ?: "",
                cellColor = TableColorCategory.values()[colorCount].colorId
            )
        )
    }
}