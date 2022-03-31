package com.example.suwon_university_community.data.entity.timetable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TimeTableLocationAndTime(
    val location: String,
    val day: DayOfTheWeek,
    val time: Pair<Int, Int>
) : Parcelable{
    fun getTimeString() : String{
        val startHour  = time.first / 60
        val endHour = time.second / 60

        return  "%02d:%02d~%02d:%02d".format(startHour, time.first - startHour*60,endHour,  time.second - endHour*60)
    }
}


fun Char.toDayOfTheWeek() : DayOfTheWeek{
    return when(this){
        '월'-> DayOfTheWeek.MON
        '화'-> DayOfTheWeek.TUE
        '수'-> DayOfTheWeek.WED
        '목'-> DayOfTheWeek.THU
        '금'-> DayOfTheWeek.FRI
        else->DayOfTheWeek.DEFAULT
    }
}


