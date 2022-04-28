package net.suwon.plus.model

import androidx.annotation.ColorRes
import net.suwon.plus.R
import net.suwon.plus.data.entity.timetable.TimeTableLocationAndTime

data class TimeTableCellModel(
    val id: Long,
    val name: String,
    val locationAndTimeList: List<TimeTableLocationAndTime>,
    val professorName: String,
    @ColorRes val cellColor : Int = R.color.colorPrimary,
    @ColorRes val textColor : Int = R.color.colorPrimaryVariant
)