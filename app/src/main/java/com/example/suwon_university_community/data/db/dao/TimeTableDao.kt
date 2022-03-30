package com.example.suwon_university_community.data.db.dao

import androidx.room.*
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCrossRefEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell

@Dao
interface TimeTableDao {

    // TODO: 서버에 테이블을 저장하고 가져오는 로직 구현
    // TODO: 테이블 삭제하는 로직 구현 : 테이블 삭제시 속한 Cell들도 삭제해줘야한다. Ref도 삭제


    @Transaction
    @Query("SELECT * FROM TimeTableEntity WHERE tableId=:tableId")
    suspend fun getTimeTableWithCell(tableId: Long): TimeTableWithCell

    @Query("SELECT * FROM TimeTableEntity")
    suspend fun getTimeTableList(): List<TimeTableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTable(timeTableEntity: TimeTableEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTableCell(timeTableCellEntity: TimeTableCellEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTableCrossRefEntity(reference: TimeTableCrossRefEntity)


    // TODO: flow를 사용하여 추가할시 데이터를 받아올수 있도록 한다.

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


    @Transaction
    suspend fun deleteTimeTableCellAtTable(tableId: Long, cellId : Long){
        deleteTimeTableCell(cellId)
        deleteTimeTableCrossRefEntity(tableId, cellId)
    }


    @Update
    suspend fun updateTimeTableCell( timeTableCellEntity: TimeTableCellEntity)

}