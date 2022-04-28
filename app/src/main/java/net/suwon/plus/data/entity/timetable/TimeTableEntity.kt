package net.suwon.plus.data.entity.timetable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import net.suwon.plus.model.CellType
import net.suwon.plus.model.TimeTableModel


@Parcelize
@Entity
data class TimeTableEntity(
    @PrimaryKey(autoGenerate = true) val tableId: Long = 0,
    val tableName: String,
    val year: Int,
    val semester: Int,
    val isDefault: Boolean
) : Parcelable {
    fun toModel() = TimeTableModel(id = tableId, type = CellType.TABLE_CELL, tableName = tableName, year = year, semester = semester, isDefault = isDefault)
}