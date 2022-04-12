package com.example.suwon_university_community.data.repository.memo

import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.entity.memo.FolderWithNotice
import kotlinx.coroutines.flow.Flow

interface MemoRepository {


    fun getFolderList() : Flow<List<FolderEntity>>

    suspend fun getFolderWithNotice( folderId : Long) : FolderWithNotice

    suspend fun insertFolder( folderEntity: FolderEntity)

    suspend fun getFolder( id : Long) : FolderEntity

    suspend fun updateFolder( folderEntity: FolderEntity )

    suspend fun deleteFolder( id: Long)

    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)

    suspend fun deleteBookMarkNotice( id : Long)

    suspend fun getFolderCount() : Int?
}