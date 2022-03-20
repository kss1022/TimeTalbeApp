package com.example.suwon_university_community.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.suwon_university_community.data.entity.lecture.LectureEntity


@Dao
interface LectureDao {

    @Query("SELECT * FROM LectureEntity")
    suspend fun getAll() : List<LectureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureList(lectureEntityList: List<LectureEntity> )
}