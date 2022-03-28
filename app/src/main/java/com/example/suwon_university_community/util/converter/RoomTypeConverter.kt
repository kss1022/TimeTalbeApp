package com.example.suwon_university_community.util.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime
import com.google.gson.Gson
import javax.inject.Inject

@ProvidedTypeConverter
class RoomTypeConverter @Inject constructor(
    private val gson: Gson
) {


    @TypeConverter
    fun listToString(value: List<TimeTableLocationAndTime>?): String? = gson.toJson(value)


    @TypeConverter
    fun stringToList(value: String?): List<TimeTableLocationAndTime>? =
        gson.fromJson(value, Array<TimeTableLocationAndTime>::class.java).toList()

}