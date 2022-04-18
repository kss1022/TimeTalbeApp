package com.example.suwon_university_community.data.db.dao

import androidx.room.*
import com.example.suwon_university_community.data.entity.memo.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemoDao {


    //FolderWith
    @Transaction
    @Query("SELECT * FROM FolderEntity WHERE folderId=:folderId")
    suspend fun getFolderWithNotice( folderId: Long) : FolderWithNotice

    @Transaction
    @Query("SELECT * FROM folderentity WHERE folderId=:folderId" )
    suspend fun  getFolderWithMemo( folderId : Long) : FolderWithMemo


    //Folder
    @Query("SELECT * FROM folderentity")
    fun getFolderList() : Flow<List<FolderEntity>>

    @Query("SELECT * FROM folderentity WHERE folderId=:id")
    suspend fun getFolder(id : Long) : FolderEntity

    @Query("SELECT COUNT(folderId) FROM folderentity")
    suspend fun getFolderCount (): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folderEntity: FolderEntity)

    @Update
    suspend fun updateFolder(folderEntity: FolderEntity)

    @Query("DELETE FROM folderentity WHERE folderId=:id")
    suspend fun deleteFolder(id : Long)

    //Bookmark
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)


    @Query( "DELETE FROM bookmarknoticeentity WHERE noticeId=:id")
    suspend fun deleteBookMarkNotice( id : Long)


    //Memo
    @Query("SELECT * FROM memoentity WHERE memoId=:id" )
    suspend fun getMemo(id: Long) : MemoEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoEntity: MemoEntity)

    @Query("DELETE FROM memoentity WHERE memoId=:id")
    suspend fun deleteMemo(id :Long)

    @Update
    suspend fun updateMemo(memoEntity: MemoEntity)

}