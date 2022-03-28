package com.example.suwon_university_community.data.entity.timetable

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.suwon_university_community.model.TimeTableCellModel
import com.example.suwon_university_community.util.converter.RoomTypeConverter
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
@TypeConverters(RoomTypeConverter::class)
data class TimeTableCellEntity(
    @PrimaryKey val cellId: Long,
    val name: String,
    val distinguish: String,
    val point : Float,
    val locationAndTimeList: List<TimeTableLocationAndTime>,
    val professorName: String,
    @ColorRes val cellColor : Int
) : Parcelable {
    fun toModel() = TimeTableCellModel(cellId, name, locationAndTimeList, professorName, cellColor)
}