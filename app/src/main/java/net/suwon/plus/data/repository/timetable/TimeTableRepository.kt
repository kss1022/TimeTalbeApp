package net.suwon.plus.data.repository.timetable

import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.data.entity.timetable.TimeTableWithCell


interface TimeTableRepository {

    suspend fun insertTimeTable( timeTableEntity: TimeTableEntity)

    suspend fun getTimeTableList() : List<TimeTableEntity>

    suspend fun getTimeTableCount() : Int?

    suspend fun insertTimeTableCellWithTable( timeTableId: Long, timeTableCellEntity: TimeTableCellEntity)

    suspend fun deleteTimeTableWithCell(timeTableId: Long)

    suspend fun getTimeTableWithCell(timeTableId : Long) : TimeTableWithCell

    suspend fun deleteTimeTableCellAtTable( timeTableId: Long, timTableCellId : Long)

    suspend fun updateTimeTable( timeTableEntity: TimeTableEntity)

    suspend fun updateTimeTableCell(timeTableCellEntity: TimeTableCellEntity)
}