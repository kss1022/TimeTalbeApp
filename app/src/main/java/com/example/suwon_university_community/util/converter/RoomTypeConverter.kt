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


//    @TypeConverter
//    fun listToString(locationAndTime: List<TimeTableLocationAndTime>): String {
//
//        var string :String = ""
//        locationAndTime.forEach { it ->
//            string = string + "/"+ locationAndTimeToString(it)
//        }
//
//        return string
//    }
//
//    @TypeConverter
//    fun stringToList(string: String): List<TimeTableLocationAndTime> {
//        val splitedStr = string.split("/")
//
//        val list : ArrayList<TimeTableLocationAndTime> = arrayListOf()
//
//        for (i in splitedStr) {
//             list.add ( stringTolocationAndTime(i) )
//        }
//
//        return list
//    }
//
//
//
//    fun locationAndTimeToString(locationAndTime: TimeTableLocationAndTime): String {
//        var string = "${locationAndTime.day},${locationAndTime.location}"
//
//        locationAndTime.time.forEach { it ->
//            string = string + "," + it
//        }
//
//        return string
//    }
//
//
//    fun stringTolocationAndTime(string: String): TimeTableLocationAndTime {
//        val splitedStr = string.split(",")
//
//
//        val timeArrayList: ArrayList<Int> = arrayListOf()
//        for (i in 2 until splitedStr.size) {
//            timeArrayList.add(splitedStr[i].toInt())
//        }
//        return TimeTableLocationAndTime(splitedStr[0], splitedStr[1], timeArrayList)
//    }