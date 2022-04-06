package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.entity.notice.NoticeEntity



interface NoticeService {

    suspend fun getUpdatedTimeMillis() : Long

    suspend fun getNoticeList() : List<NoticeEntity>

}