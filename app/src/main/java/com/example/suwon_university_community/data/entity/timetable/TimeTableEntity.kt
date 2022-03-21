package com.example.suwon_university_community.data.entity.timetable

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class TimeTableEntity (
    @PrimaryKey val tableId : Long,
    val tableName : String,
    val year : Int,
    val semester : Int,
    val isDefault: Boolean
) : Parcelable