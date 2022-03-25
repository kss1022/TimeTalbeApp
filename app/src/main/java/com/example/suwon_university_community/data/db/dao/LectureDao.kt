package com.example.suwon_university_community.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.suwon_university_community.data.entity.lecture.LectureEntity


@Dao
interface LectureDao {

    // TODO: Paging을 사용해서 가져오기
    @Query("SELECT * FROM lectureentity")
    suspend fun getAll() : List<LectureEntity>

    @Query("SELECT * FROM lectureentity WHERE department=:department")
    suspend fun getLectureList( department: String) : List<LectureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureList(lectureEntityList: List<LectureEntity> )
}