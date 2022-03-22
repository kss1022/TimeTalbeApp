package com.example.suwon_university_community.data.entity.timetable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TimeTableLocationAndTime(
    val location: String,
    val day: Char,
    val time: List<Int>
) : Parcelable