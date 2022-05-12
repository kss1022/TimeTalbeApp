package net.suwon.plus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.suwon.plus.data.db.dao.LectureDao
import net.suwon.plus.data.db.dao.MemoDao
import net.suwon.plus.data.db.dao.NoticeDao
import net.suwon.plus.data.db.dao.TimeTableDao
import net.suwon.plus.data.entity.lecture.LectureEntity
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.entity.memo.MemoEntity
import net.suwon.plus.data.entity.notice.NoticeEntity
import net.suwon.plus.data.entity.timetable.TimeTableCellEntity
import net.suwon.plus.data.entity.timetable.TimeTableCrossRefEntity
import net.suwon.plus.data.entity.timetable.TimeTableEntity

@Database(
    entities = [
        LectureEntity::class,
        TimeTableCellEntity::class, TimeTableEntity::class, TimeTableCrossRefEntity::class,
        NoticeEntity::class,
        FolderEntity::class,
        BookMarkNoticeEntity::class,
        MemoEntity::class
    ],  version = 2, exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getLectureDao(): LectureDao
    abstract fun getTimeTableDao(): TimeTableDao
    abstract fun getNoticeDao() : NoticeDao
    abstract fun getMemoDao() : MemoDao




    companion object {
        const val APP_DATABASE_NAME = "suwon_university_database"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE MemoEntity ADD COLUMN imageUrlList TEXT NOT NULL DEFAULT '[]' ")
            }
        }
    }
}