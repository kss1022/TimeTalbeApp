package com.example.suwon_university_community.data.repository.notice

import com.example.suwon_university_community.data.api.NoticeService
import com.example.suwon_university_community.data.db.dao.NoticeDao
import com.example.suwon_university_community.data.entity.notice.NoticeEntity
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.ui.main.home.notice.NoticeCategory
import com.example.suwon_university_community.util.provider.ResourceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


// TODO: 의존성 주입하기 
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

    override suspend fun getNoticeList(categoty : String ): List<NoticeEntity>  = withContext(ioDispatcher){
        if(categoty == resourceProvider.getString( NoticeCategory.ALL.categoryNameId)){
            noticeDao.getAll()
        }else{
            noticeDao.getNoticeList(categoty)
        }
    }
}