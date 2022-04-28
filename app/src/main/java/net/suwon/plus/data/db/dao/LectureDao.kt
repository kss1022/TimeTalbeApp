package net.suwon.plus.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.suwon.plus.data.entity.lecture.LectureEntity


@Dao
interface LectureDao {

    @Query("SELECT * FROM lectureentity")
    suspend fun getAll() : List<LectureEntity>

    @Query("SELECT * FROM lectureentity WHERE department=:department")
    suspend fun getLectureList( department: String) : List<LectureEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectureList(lectureEntityList: List<LectureEntity> )
}