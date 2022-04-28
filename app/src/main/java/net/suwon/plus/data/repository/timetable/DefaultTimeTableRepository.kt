package net.suwon.plus.data.repository.timetable

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.suwon.plus.data.db.dao.MemoDao
import net.suwon.plus.data.db.dao.TimeTableDao
import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableEntity
import net.suwon.plus.data.entity.timetable.TimeTableWithCell
import javax.inject.Inject

class DefaultTimeTableRepository @Inject constructor(
    private val timeTableDao: TimeTableDao,
    private val memoDao: MemoDao,
    private val ioDispatcher: CoroutineDispatcher
) : TimeTableRepository {

    override suspend fun insertTimeTable(timeTableEntity: TimeTableEntity) =
        withContext(ioDispatcher) {
            timeTableDao.insertTimeTable(timeTableEntity)
        }

    override suspend fun getTimeTableList(): List<TimeTableEntity> = withContext(ioDispatcher) {
        timeTableDao.getTimeTableList()
    }

    override suspend fun getTimeTableCount(): Int? = withContext(ioDispatcher) {
        timeTableDao.getTimeTableCount()
    }

    override suspend fun insertTimeTableCellWithTable(
        timeTableId: Long,
        timeTableCellEntity: TimeTableCellEntity
    ) = withContext(ioDispatcher) {
        timeTableDao.insertTimeTableCellWithTable(timeTableId, timeTableCellEntity)
    }

    override suspend fun deleteTimeTableWithCell(timeTableId: Long) = withContext(ioDispatcher) {
        val cellIdList =
            timeTableDao.getTimeTableWithCell(timeTableId).timeTableCellList.map { it.cellId }

        timeTableDao.deleteTimeTableWithCell(timeTableId, cellIdList)

        //Memo Edit
        val folderList = memoDao.getFolderList(timeTableId)

        folderList.forEach { folder ->
            memoDao.getFolderWithMemo(folder.folderId).memos.forEach {
                val defaultFolder = memoDao.getFolder(2)
                memoDao.updateMemo(it.copy(memoFolderId = 2, timeTableCellId = null))

                memoDao.updateFolder(defaultFolder.copy(count =  defaultFolder.count +1))
            }
            memoDao.deleteFolder(folder.folderId)
        }
    }


    override suspend fun getTimeTableWithCell(timeTableId: Long): TimeTableWithCell =
        withContext(ioDispatcher) {
            timeTableDao.getTimeTableWithCell(timeTableId)
        }

    override suspend fun deleteTimeTableCellAtTable(timeTableId: Long, timTableCellId: Long) =
        withContext(ioDispatcher) {
            timeTableDao.deleteTimeTableCellAtTable(timeTableId, timTableCellId)

            //memo Edit
            memoDao.getMemoList(timTableCellId).forEach {
                memoDao.updateMemo(it.copy(timeTableCellId = null))
            }
        }


    override suspend fun updateTimeTable(timeTableEntity: TimeTableEntity) {
        timeTableDao.updateTimeTable(timeTableEntity)
    }

    override suspend fun updateTimeTableCell(timeTableCellEntity: TimeTableCellEntity) =
        withContext(ioDispatcher) {
            timeTableDao.updateTimeTableCell(timeTableCellEntity)
        }
}