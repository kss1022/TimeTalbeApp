package com.example.suwon_university_community.data.repository.memo

import com.example.suwon_university_community.data.db.dao.MemoDao
import com.example.suwon_university_community.data.entity.memo.*
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
    override suspend fun getFolderWithNotice(folderId: Long): FolderWithNotice =
        withContext(ioDispatcher) {
            memoDao.getFolderWithNotice(folderId)
        }

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
        memoDao.deleteFolder(id)
    }


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
}