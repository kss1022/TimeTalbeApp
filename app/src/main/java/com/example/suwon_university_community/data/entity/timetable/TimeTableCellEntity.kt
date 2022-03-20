package com.example.suwon_university_community.data.entity.timetable

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.suwon_university_community.model.TimeTableCellModel
import com.example.suwon_university_community.util.converter.RoomTypeConverter

@Entity
@TypeConverters(RoomTypeConverter::class)
data class TimeTableCellEntity (
    @PrimaryKey val cellId : Long,
    val name : String,
    val distinguish :String,
    val grade : Int,
    val locationAndTimeList : List<TimeTableLocationAndTime>,
    val professorName : String,
    val timeTableId : Int
){
    fun toModel() =  TimeTableCellModel(cellId, name, locationAndTimeList, professorName, timeTableId)
}