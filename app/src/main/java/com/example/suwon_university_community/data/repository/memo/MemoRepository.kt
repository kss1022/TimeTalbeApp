package com.example.suwon_university_community.data.repository.memo

import com.example.suwon_university_community.data.entity.memo.*
import kotlinx.coroutines.flow.Flow

interface MemoRepository {

    //FolderWith
    suspend fun getFolderWithNotice( folderId : Long) : FolderWithNotice

    suspend fun getFolderWithMemo( folderId: Long) : FolderWithMemo


    //Folder
    fun getFolderList() : Flow<List<FolderEntity>>

    suspend fun getFolder( id : Long) : FolderEntity

    suspend fun getFolderCount() : Int?

    suspend fun insertFolder( folderEntity: FolderEntity)

    suspend fun updateFolder( folderEntity: FolderEntity )

    suspend fun deleteFolder( id: Long)


    //Bookmark
    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)

    suspend fun deleteBookMarkNotice( id : Long)

    //Memo
    suspend fun insertMemo(memoEntity: MemoEntity)

    suspend fun updateMemo(memoEntity: MemoEntity)
}