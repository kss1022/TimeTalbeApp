package com.example.suwon_university_community.data.entity.timetable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suwon_university_community.model.CellType
import com.example.suwon_university_community.model.TimeTableModel
import kotlinx.parcelize.Parcelize


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