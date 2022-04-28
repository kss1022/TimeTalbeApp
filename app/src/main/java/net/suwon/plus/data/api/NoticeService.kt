package net.suwon.plus.data.api

import net.suwon.plus.data.entity.notice.NoticeEntity


interface NoticeService {

    suspend fun getUpdatedTimeMillis() : Long

    suspend fun getNoticeList() : List<NoticeEntity>

}