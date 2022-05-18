package net.suwon.plus.util.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import net.suwon.plus.data.entity.memo.MemoImage
import net.suwon.plus.data.entity.timetable.TimeTableLocationAndTime
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



    @TypeConverter
    fun urlListToString(value: List<MemoImage>?): String? = gson.toJson(value)


    @TypeConverter
    fun urlStringToList(value: String?): List<MemoImage>? =
        gson.fromJson(value, Array<MemoImage>::class.java).toList()

}