package com.example.suwon_university_community.data.repository.timetable

import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell


interface TimeTableRepository {

    suspend fun insertTimeTable( timeTableEntity: TimeTableEntity)

    suspend fun getTimeTableList() : List<TimeTableEntity>


    suspend fun insertTimeTableCellWithTable( timeTableId: Long, timeTableCellEntity: TimeTableCellEntity)

    suspend fun getTimeTableWithCell(timeTableId : Long) :  TimeTableWithCell


}