package com.example.suwon_university_community.data.repository.memo

import com.example.suwon_university_community.data.db.dao.MemoDao
import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.entity.memo.FolderWithNotice
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
    override fun getFolderList(): Flow<List<FolderEntity>> =
        memoDao.getFolderList()
            .distinctUntilChanged()
            .flowOn(ioDispatcher)

    override suspend fun getFolderWithNotice(folderId: Long): FolderWithNotice =
        withContext(ioDispatcher) {
            memoDao.getFolderWithNotice(folderId)
        }

    override suspend fun insertFolder(folderEntity: FolderEntity) = withContext(ioDispatcher) {
        memoDao.insertFolder(folderEntity)
    }

    override suspend fun getFolder(id: Long): FolderEntity = withContext(ioDispatcher) {
        memoDao.getFolderList(id)
    }

    override suspend fun updateFolder(folderEntity: FolderEntity) = withContext(ioDispatcher) {
        memoDao.updateFolder(folderEntity)
    }

    override suspend fun deleteFolder(id: Long) = withContext(ioDispatcher) {
        memoDao.deleteFolder(id)
    }


    override suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity) =
        withContext(ioDispatcher) {
            memoDao.insertBookMarkNotice(bookMarkNoticeEntity)

            val noticeFolder = getFolder(bookMarkNoticeEntity.noticeFolderId)

            memoDao.updateFolder(
                FolderEntity(
                    folderId = noticeFolder.folderId,
                    name = noticeFolder.name,
                    count = noticeFolder.count + 1,
                    category = noticeFolder.category,
                    isDefault = noticeFolder.isDefault,
                )
            )
        }

    override suspend fun deleteBookMarkNotice(id: Long) = withContext(ioDispatcher) {
        memoDao.deleteBookMarkNotice(id)
    }

    override suspend fun getFolderCount(): Int? = withContext(ioDispatcher) {
        memoDao.getFolderCount()
    }
}