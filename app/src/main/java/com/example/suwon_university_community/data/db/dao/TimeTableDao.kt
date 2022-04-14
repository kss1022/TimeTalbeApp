package com.example.suwon_university_community.data.db.dao

import androidx.room.*
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCrossRefEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell

@Dao
interface TimeTableDao {


    @Transaction
    @Query("SELECT * FROM TimeTableEntity WHERE tableId=:tableId")
    suspend fun getTimeTableWithCell(tableId: Long): TimeTableWithCell

    @Query("SELECT * FROM TimeTableEntity")
    suspend fun getTimeTableList(): List<TimeTableEntity>

    @Query("SELECT COUNT(tableId) FROM TimeTableEntity")
    suspend fun getTimeTableCount() : Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTable(timeTableEntity: TimeTableEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTableCell(timeTableCellEntity: TimeTableCellEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTableCrossRefEntity(reference: TimeTableCrossRefEntity)


    @Transaction
    suspend fun insertTimeTableCellWithTable(
        tableId: Long,
        timeTableCellEntity: TimeTableCellEntity
    ) {
        insertTimeTableCell(timeTableCellEntity)

        insertTimeTableCrossRefEntity(
            TimeTableCrossRefEntity(
                tableId,
                timeTableCellEntity.cellId
            )
        )
    }


    @Query("DELETE FROM timetablecellentity WHERE cellId=:cellId")
    suspend fun deleteTimeTableCell(cellId: Long)

    @Query("DELETE FROM timetablecrossrefentity WHERE tableId=:tableId AND cellId=:cellId")
    suspend fun deleteTimeTableCrossRefEntity(tableId: Long, cellId: Long)

    @Query("DELETE FROM TimeTableEntity WHERE tableId=:tableId")
    suspend fun deleteTimeTable(tableId: Long)


    @Transaction
    suspend fun deleteTimeTableCellAtTable(tableId: Long, cellId : Long){
        deleteTimeTableCell(cellId)
        deleteTimeTableCrossRefEntity(tableId, cellId)
    }

    @Transaction
    suspend fun deleteTImeTableWithCell(tableId: Long , cellIdList : List<Long>){
        cellIdList.forEach {
            deleteTimeTableCellAtTable(tableId, it)
        }
        deleteTimeTable(tableId)
    }


    @Update
    suspend fun updateTimeTable ( timeTableEntity: TimeTableEntity)

    @Update
    suspend fun updateTimeTableCell( timeTableCellEntity: TimeTableCellEntity)

}