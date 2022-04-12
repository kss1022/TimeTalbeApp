package com.example.suwon_university_community.data.db.dao

import androidx.room.*
import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.entity.memo.FolderWithNotice
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {

    @Query("SELECT * FROM FolderEntity")
    fun getFolderList() : Flow<List<FolderEntity>>

    @Query("SELECT * FROM FolderEntity WHERE folderId=:id")
    suspend fun getFolderList( id : Long) : FolderEntity

    @Transaction
    @Query("SELECT * FROM FolderEntity WHERE folderId=:folderId")
    suspend fun getFolderWithNotice( folderId: Long) : FolderWithNotice

    @Query("SELECT * FROM bookmarknoticeentity WHERE noticeId=:id")
    suspend fun getBookMarkNotice( id : Long) : BookMarkNoticeEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folderEntity: FolderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)

    @Query("DELETE FROM folderentity WHERE folderId=:id")
    suspend fun deleteFolder(id : Long)

    @Query( "DELETE FROM bookmarknoticeentity WHERE noticeId=:id")
    suspend fun deleteBookMarkNotice( id : Long)

    @Update
    suspend fun updateFolder(folderEntity: FolderEntity)


    @Query("SELECT COUNT  FROM folderentity")
    suspend fun getFolderCount (): Int?
}