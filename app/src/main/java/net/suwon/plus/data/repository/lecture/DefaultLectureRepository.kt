package net.suwon.plus.data.repository.lecture


import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.suwon.plus.data.api.TimeTableService
import net.suwon.plus.data.db.dao.LectureDao
import net.suwon.plus.data.entity.lecture.CollegeCategory
import net.suwon.plus.data.entity.lecture.LectureEntity
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.util.provider.ResourceProvider
import javax.inject.Inject

class DefaultLectureRepository  @Inject constructor(
    private val timeTableService: TimeTableService,
    private val preferenceManager: PreferenceManager,
    private val lectureDao: LectureDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val resourceProvider: ResourceProvider
) : LectureRepository {

    override suspend fun refreshLecture() = withContext(ioDispatcher) {
        val fileUpdatedTimeMillis = timeTableService.getUpdatedTimeMillis()
        val lastDatabaseUpdatedTIme = preferenceManager.getTimeTableUpdatedTime()


        if (lastDatabaseUpdatedTIme == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTIme) {
            val updateData = timeTableService.getTimeTableList()

            if (updateData.isNullOrEmpty()) {
                Log.e("TimeRepository", "TimeTable is Empty")
                return@withContext
            }

            lectureDao.insertLectureList(updateData)


            preferenceManager.putTimeTableUpdatedTime(fileUpdatedTimeMillis)
            Log.d("TimeRepository", "Updated Success")
        }
    }




    override suspend fun getLectureList(department: String): List<LectureEntity> = withContext(ioDispatcher){
        if( department ==  resourceProvider.getString( CollegeCategory.ALL.categoryNameId )) {
            lectureDao.getAll()
        } else{
            lectureDao.getLectureList(department)
        }
    }
}