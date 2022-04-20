package com.example.suwon_university_community.data.repository.memo

import com.example.suwon_university_community.data.db.dao.MemoDao
import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.entity.memo.FolderWithMemo
import com.example.suwon_university_community.data.entity.memo.MemoEntity
import com.example.suwon_university_community.model.MemoModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMemoRepository @Inject constructor(
    private val memoDao: MemoDao,
    private val ioDispatcher: CoroutineDispatcher
) : MemoRepository {

    //FolderWith
    override suspend fun getFolderWithMemo(folderId: Long): FolderWithMemo =
        withContext(ioDispatcher) {
            memoDao.getFolderWithMemo(folderId)
        }


    //Folder
    override fun getFolderList(): Flow<List<FolderEntity>> =
        memoDao.getFolderList()
            .distinctUntilChanged()
            .flowOn(ioDispatcher)


    override suspend fun getFolder(id: Long): FolderEntity = withContext(ioDispatcher) {
        memoDao.getFolder(id)
    }


    override suspend fun getFolderCount(): Int? = withContext(ioDispatcher) {
        memoDao.getFolderCount()
    }

    override suspend fun insertFolder(folderEntity: FolderEntity) = withContext(ioDispatcher) {
        memoDao.insertFolder(folderEntity)
    }


    override suspend fun updateFolder(folderEntity: FolderEntity) = withContext(ioDispatcher) {
        memoDao.updateFolder(folderEntity)
    }

    override suspend fun deleteFolder(id: Long) = withContext(ioDispatcher) {
        val memos = memoDao.getFolderWithMemo(id).memos

        memos.forEach { memoDao.deleteMemo(it.memoId) }
        memoDao.deleteFolder(id)
    }

    override fun getBookMarkList(): Flow<List<BookMarkNoticeEntity>> =
        memoDao.getBookMarkList()
            .distinctUntilChanged()
            .flowOn(ioDispatcher)


    //Bookmark
    override suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity) =
        withContext(ioDispatcher) {
            memoDao.insertBookMarkNotice(bookMarkNoticeEntity)

            val noticeFolder = getFolder(bookMarkNoticeEntity.noticeFolderId)
            memoDao.updateFolder(
                noticeFolder.copy(count = noticeFolder.count+1)
            )
        }

    override suspend fun deleteBookMarkNotice(id: Long) = withContext(ioDispatcher) {
        memoDao.deleteBookMarkNotice(id)

        val noticeFolder = getFolder(1)
        memoDao.updateFolder(
            noticeFolder.copy(count = noticeFolder.count -1)
        )
    }

    override suspend fun getMemo(id: Long): MemoEntity  = withContext(ioDispatcher){
        memoDao.getMemo(id)
    }

    //Memo
    override suspend fun insertMemo(memoEntity: MemoEntity) = withContext(ioDispatcher) {
        memoDao.insertMemo(memoEntity)

        val memoFolder = getFolder(memoEntity.memoFolderId)
        memoDao.updateFolder(
            memoFolder.copy(count = memoFolder.count + 1)
        )
    }

    override suspend fun updateMemo(memoEntity: MemoEntity) = withContext(ioDispatcher) {
        memoDao.updateMemo(memoEntity)
    }

    override suspend fun changeFolder(memoModel: MemoModel, folderId: Long) {
        memoDao.updateMemo(MemoEntity(
            memoId = memoModel.id,
            title = memoModel.title,
            memo = memoModel.memo,
            time = memoModel.time,
            memoFolderId = folderId,
            timeTableCellId = memoModel.timeTableCellId
        ))

        val currentFolder = getFolder(memoModel.memoFolderId)
        val changeFolder  = getFolder(folderId)

        memoDao.updateFolder(currentFolder.copy(count = currentFolder.count-1))
        memoDao.updateFolder(changeFolder.copy(count = changeFolder.count+1))
    }

    override suspend fun deleteMemo(memoModel: MemoModel) {
        memoDao.deleteMemo(memoModel.id)

        val memoFolder = memoDao.getFolder(memoModel.memoFolderId)
        memoDao.updateFolder(
            memoFolder.copy(count = memoFolder.count-1)
        )
    }
}