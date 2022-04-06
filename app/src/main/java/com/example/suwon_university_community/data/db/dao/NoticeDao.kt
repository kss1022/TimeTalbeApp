package com.example.suwon_university_community.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.suwon_university_community.data.entity.notice.NoticeEntity

@Dao
interface NoticeDao {

    @Query("SELECT * FROM noticeentity")
    suspend fun getAll() : List<NoticeEntity>


    @Query("SELECT * FROM noticeentity WHERE category=:category")
    suspend fun getNoticeList( category: String) : List<NoticeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoticeList( noticeList : List<NoticeEntity>)
}