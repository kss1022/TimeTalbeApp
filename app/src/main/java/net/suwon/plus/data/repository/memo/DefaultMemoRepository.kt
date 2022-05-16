package net.suwon.plus.data.repository.memo

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import net.suwon.plus.data.db.dao.MemoDao
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.entity.memo.FolderWithMemo
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.model.MemoModel
import java.io.File
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
                noticeFolder.copy(count = noticeFolder.count + 1)
            )
        }

    override suspend fun deleteBookMarkNotice(id: Long) = withContext(ioDispatcher) {
        memoDao.deleteBookMarkNotice(id)

        val noticeFolder = getFolder(1)
        memoDao.updateFolder(
            noticeFolder.copy(count = noticeFolder.count - 1)
        )
    }

    override suspend fun getMemo(id: Long): MemoEntity = withContext(ioDispatcher) {
        memoDao.getMemo(id)
    }

    //Memo
    override suspend fun insertMemo(memoEntity: MemoEntity) : Long = withContext(ioDispatcher) {
        val id = memoDao.insertMemo(memoEntity)

        val memoFolder = getFolder(memoEntity.memoFolderId)
        memoDao.updateFolder(
            memoFolder.copy(count = memoFolder.count + 1)
        )

        id
    }

    override suspend fun updateMemo(memoEntity: MemoEntity) = withContext(ioDispatcher) {
        memoDao.updateMemo(memoEntity)
    }

    override suspend fun changeFolder(model: MemoModel, folderId: Long) {
        memoDao.updateMemo(
            MemoEntity(
                memoId = model.id,
                title = model.title,
                memo = model.memo,
                time = model.time,
                memoFolderId = folderId,
                timeTableCellId = model.timeTableCellId
            )
        )

        val currentFolder = getFolder(model.memoFolderId)
        val changeFolder = getFolder(folderId)

        memoDao.updateFolder(currentFolder.copy(count = currentFolder.count - 1))
        memoDao.updateFolder(changeFolder.copy(count = changeFolder.count + 1))
    }

    override suspend fun deleteMemo(memoModel: MemoModel) {
        memoDao.deleteMemo(memoModel.id)

        memoModel.imageUrlList.forEach { url ->
            File(url).delete()
        }


        val memoFolder = memoDao.getFolder(memoModel.memoFolderId)
        memoDao.updateFolder(
            memoFolder.copy(count = memoFolder.count - 1)
        )
    }

    override suspend fun deleteMemo(id: Long) {
        val memoModel = memoDao.getMemo(id)

        memoDao.deleteMemo(id)

        memoModel.imageUrlList.forEach { url ->
            File(url).delete()
        }


        val memoFolder = memoDao.getFolder(memoModel.memoFolderId)
        memoDao.updateFolder(
            memoFolder.copy(count = memoFolder.count - 1)
        )
    }
}