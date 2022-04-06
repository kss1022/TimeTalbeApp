package com.example.suwon_university_community.data.repository.notice

import com.example.suwon_university_community.data.entity.notice.NoticeEntity

interface NoticeRepository {

    suspend fun refreshNotice()

    suspend fun getNoticeList( category : String) : List<NoticeEntity>
}