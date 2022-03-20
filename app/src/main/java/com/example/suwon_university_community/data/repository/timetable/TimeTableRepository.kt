package com.example.suwon_university_community.data.repository.timetable

import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity


interface TimeTableRepository {

    suspend fun insertTimeTable( timeTableEntity: TimeTableEntity)

    suspend fun insertTimeTableCellWithTable( timeTableId: Long, timeTableCellEntity: TimeTableCellEntity)

    suspend fun getTimeTableCellList(timeTableId : Long) :  List<TimeTableCellEntity>
}