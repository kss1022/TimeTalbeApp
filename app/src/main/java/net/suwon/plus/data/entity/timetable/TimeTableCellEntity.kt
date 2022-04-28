package net.suwon.plus.data.entity.timetable

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import net.suwon.plus.R
import net.suwon.plus.model.TimeTableCellModel
import net.suwon.plus.util.converter.RoomTypeConverter

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
    @ColorRes val cellColor : Int = R.color.colorPrimary,
    @ColorRes val textColor : Int = R.color.colorPrimaryVariant
) : Parcelable {
    fun toModel() = TimeTableCellModel(cellId, name, locationAndTimeList, professorName, cellColor, textColor)
}