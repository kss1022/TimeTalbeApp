package com.example.suwon_university_community.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.suwon_university_community.data.db.dao.LectureDao
import com.example.suwon_university_community.data.db.dao.NoticeDao
import com.example.suwon_university_community.data.db.dao.TimeTableDao
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.example.suwon_university_community.data.entity.notice.NoticeEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCrossRefEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity

@Database(
    entities = [
        LectureEntity::class,
        TimeTableCellEntity::class, TimeTableEntity::class, TimeTableCrossRefEntity::class,
        NoticeEntity::class
    ],  version = 1, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getLectureDao(): LectureDao
    abstract fun getTimeTableDao(): TimeTableDao
    abstract fun getNoticeDao() : NoticeDao

    companion object {
        const val APP_DATABASE_NAME = "suwon_university_database"
    }
}