package com.example.suwon_university_community.data.entity.timetable


data class TimeTableLocationAndTime(
    val location: String,
    val day: String,
    val time: List<Int>
)