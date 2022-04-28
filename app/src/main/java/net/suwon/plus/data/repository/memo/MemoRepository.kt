package net.suwon.plus.data.repository.memo

import kotlinx.coroutines.flow.Flow
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.entity.memo.FolderWithMemo
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.model.MemoModel

interface MemoRepository {

    //FolderWith
    suspend fun getFolderWithMemo( folderId: Long) : FolderWithMemo


    //Folder
    fun getFolderList() : Flow<List<FolderEntity>>

    suspend fun getFolder( id : Long) : FolderEntity

    suspend fun getFolderCount() : Int?

    suspend fun insertFolder( folderEntity: FolderEntity)

    suspend fun updateFolder( folderEntity: FolderEntity )

    suspend fun deleteFolder( id: Long)


    //Bookmark
    fun getBookMarkList() : Flow<List<BookMarkNoticeEntity>>

    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)

    suspend fun deleteBookMarkNotice( id : Long)

    //Memo
    suspend fun getMemo(id : Long) : MemoEntity

    suspend fun insertMemo(memoEntity: MemoEntity)

    suspend fun updateMemo(memoEntity: MemoEntity)

    suspend fun changeFolder(memoEntity: MemoModel, folderId: Long)

    suspend fun deleteMemo(memoModel: MemoModel)
}