package com.example.suwon_university_community.data.repository.lecture


import android.util.Log
import com.example.suwon_university_community.data.api.TimeTableService
import com.example.suwon_university_community.data.db.dao.LectureDao
import com.example.suwon_university_community.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultLectureRepository  @Inject constructor(
    private val timeTableService: TimeTableService,
    private val preferenceManager: PreferenceManager,
    private val lectureDao: LectureDao,
    private val ioDispatcher: CoroutineDispatcher
) : LectureRepository {

    override suspend fun refreshLecture() = withContext(ioDispatcher) {
        val fileUpdatedTimeMillis = timeTableService.getUpdatedTimeMillis()
        val lastDatabaseUpdatedTIme = preferenceManager.getUpdatedTime()


        if (lastDatabaseUpdatedTIme == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTIme) {
            val updateData = timeTableService.getTimeTableList()

            if (updateData.isNullOrEmpty()) {
                Log.e("TimeRepository", "TimeTable is Empty")
                return@withContext
            }


            lectureDao.insertLectureList(updateData)
            preferenceManager.putUpdatedTime(fileUpdatedTimeMillis)
            Log.d("TimeRepository", "Updated Success")
        }
    }


}