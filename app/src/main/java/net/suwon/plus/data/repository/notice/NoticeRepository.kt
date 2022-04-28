package net.suwon.plus.data.repository.notice

import net.suwon.plus.data.entity.notice.NoticeEntity

interface NoticeRepository {

    suspend fun refreshNotice()

    suspend fun getNoticeList( category : String) : List<NoticeEntity>
}