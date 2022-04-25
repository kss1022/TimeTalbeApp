package com.example.suwon_university_community.model

import androidx.annotation.ColorRes
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime

data class TimeTableCellModel(
    val id: Long,
    val name: String,
    val locationAndTimeList: List<TimeTableLocationAndTime>,
    val professorName: String,
    @ColorRes val cellColor : Int = R.color.colorPrimary,
    @ColorRes val textColor : Int = R.color.colorPrimaryVariant
)