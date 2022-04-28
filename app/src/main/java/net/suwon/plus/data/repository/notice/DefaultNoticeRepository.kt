package net.suwon.plus.data.repository.notice

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import net.suwon.plus.data.api.NoticeService
import net.suwon.plus.data.db.dao.NoticeDao
import net.suwon.plus.data.entity.notice.NoticeEntity
import net.suwon.plus.data.preference.PreferenceManager
import net.suwon.plus.ui.main.home.notice.NoticeCategory
import net.suwon.plus.util.provider.ResourceProvider
import javax.inject.Inject


class DefaultNoticeRepository @Inject constructor(
    private val noticeService: NoticeService,
    private val noticeDao: NoticeDao,
    private val preferenceManager: PreferenceManager,
    private val resourceProvider: ResourceProvider,
    private val ioDispatcher: CoroutineDispatcher
) : NoticeRepository {
    override suspend fun refreshNotice() = withContext(ioDispatcher) {
        val fileUpdatedTime = noticeService.getUpdatedTimeMillis()
        val lastUpdatedTime = preferenceManager.getNoticeUpdatedTime()

        if( lastUpdatedTime == null || fileUpdatedTime > lastUpdatedTime){
            val updateData = noticeService.getNoticeList()

            if(updateData.isNullOrEmpty()){
                return@withContext
            }

            noticeDao.insertNoticeList(updateData)
            preferenceManager.putNoticeUpdatedTime(fileUpdatedTime)
        }
    }

    override suspend fun getNoticeList(category : String ): List<NoticeEntity>  = withContext(ioDispatcher){
        if(category == resourceProvider.getString( NoticeCategory.ALL.categoryNameId)){
            noticeDao.getAll()
        }else{
            noticeDao.getNoticeList(category)
        }
    }
}