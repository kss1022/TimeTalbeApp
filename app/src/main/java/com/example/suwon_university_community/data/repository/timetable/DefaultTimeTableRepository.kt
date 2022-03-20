package com.example.suwon_university_community.data.repository.timetable

import com.example.suwon_university_community.data.db.dao.TimeTableDao
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultTimeTableRepository(
    private val timeTableDao: TimeTableDao,
    private val ioDispatcher: CoroutineDispatcher
) : TimeTableRepository {

    override suspend fun insertTimeTable(timeTableEntity: TimeTableEntity) = withContext(ioDispatcher){
        timeTableDao.insertTimeTable(timeTableEntity)
    }

    override suspend fun insertTimeTableCellWithTable(
        timeTableId: Long,
        timeTableEntity: TimeTableCellEntity
    )  = withContext(ioDispatcher){
        timeTableDao.insertTimeTableCellWithTable(timeTableId, timeTableEntity)
    }


    override suspend fun getTimeTableCellList(tableId: Long): List<TimeTableCellEntity> = withContext(ioDispatcher) {
        timeTableDao.getTimeTableCellList(tableId)
    }
}