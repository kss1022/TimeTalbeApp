package com.example.suwon_university_community.data.repository.timetable

import com.example.suwon_university_community.data.db.dao.TimeTableDao
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultTimeTableRepository @Inject constructor(
    private val timeTableDao: TimeTableDao,
    private val ioDispatcher: CoroutineDispatcher
) : TimeTableRepository {

    override suspend fun insertTimeTable(timeTableEntity: TimeTableEntity) = withContext(ioDispatcher){
        timeTableDao.insertTimeTable(timeTableEntity)
    }

    override suspend fun getTimeTableList(): List<TimeTableEntity> = withContext(ioDispatcher){
        timeTableDao.getTimeTableList()
    }

    override suspend fun getTimeTableCount(): Int?  = withContext(ioDispatcher){
        timeTableDao.getTimeTableCount()
    }

    override suspend fun insertTimeTableCellWithTable(
        timeTableId: Long,
        timeTableCellEntity: TimeTableCellEntity
    )  = withContext(ioDispatcher){
        timeTableDao.insertTimeTableCellWithTable(timeTableId, timeTableCellEntity)
    }

    override suspend fun deleteTimeTableWithCell(timeTableId: Long) = withContext(ioDispatcher){
        val cellIdList = timeTableDao.getTimeTableWithCell(timeTableId).timeTableCellList.map { it.cellId }

        timeTableDao.deleteTimeTableWithCell( timeTableId,cellIdList )
    }


    override suspend fun getTimeTableWithCell(timeTableId: Long): TimeTableWithCell = withContext(ioDispatcher){
        timeTableDao.getTimeTableWithCell( timeTableId)
    }

    override suspend fun deleteTimeTableCellAtTable(timeTableId: Long, timTableCellId: Long) = withContext(ioDispatcher) {
        timeTableDao.deleteTimeTableCellAtTable(timeTableId, timTableCellId)
    }


    override suspend fun updateTimeTable(timeTableEntity: TimeTableEntity) {
        timeTableDao.updateTimeTable(timeTableEntity)
    }

    override suspend fun updateTimeTableCell(timeTableCellEntity: TimeTableCellEntity) = withContext(ioDispatcher) {
        timeTableDao.updateTimeTableCell(timeTableCellEntity)
    }
}