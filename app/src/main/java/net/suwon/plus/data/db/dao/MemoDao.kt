package net.suwon.plus.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.entity.memo.FolderWithMemo
import net.suwon.plus.data.entity.memo.MemoEntity

@Dao
interface MemoDao {


    //FolderWith

    @Transaction
    @Query("SELECT * FROM folderentity WHERE folderId=:folderId" )
    suspend fun  getFolderWithMemo( folderId : Long) : FolderWithMemo


    //Folder
    @Query("SELECT * FROM folderentity")
    fun getFolderList() : Flow<List<FolderEntity>>

    @Query("SELECT * FROM folderentity WHERE folderId=:id")
    suspend fun getFolder(id : Long) : FolderEntity

    @Query("SELECT * FROM folderentity WHERE timeTableId=:timeTableId")
    suspend fun getFolderList(timeTableId : Long) : List<FolderEntity>

    @Query("SELECT COUNT(folderId) FROM folderentity")
    suspend fun getFolderCount (): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folderEntity: FolderEntity)

    @Update
    suspend fun updateFolder(folderEntity: FolderEntity)

    @Query("DELETE FROM folderentity WHERE folderId=:id")
    suspend fun deleteFolder(id : Long)

    //Bookmark
    @Query("SELECT * FROM BookMarkNoticeEntity")
    fun getBookMarkList() : Flow<List<BookMarkNoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMarkNotice(bookMarkNoticeEntity: BookMarkNoticeEntity)


    @Query( "DELETE FROM bookmarknoticeentity WHERE noticeId=:id")
    suspend fun deleteBookMarkNotice( id : Long)


    //Memo
    @Query("SELECT * FROM memoentity WHERE memoId=:id" )
    suspend fun getMemo(id: Long) : MemoEntity

    @Query("SELECT * FROM memoentity WHERE timeTableCellId=:timeTableCellId" )
    suspend fun getMemoList(timeTableCellId : Long) : List<MemoEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoEntity: MemoEntity) : Long

    @Query("DELETE FROM memoentity WHERE memoId=:id")
    suspend fun deleteMemo(id :Long)

    @Update
    suspend fun updateMemo(memoEntity: MemoEntity)

}