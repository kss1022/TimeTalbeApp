package com.example.suwon_university_community.model

import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime

data class TimeTableCellModel(
    val id: Long,
    val name: String,
    val locationAndTimeList: List<TimeTableLocationAndTime>,
    val professorName: String,
)