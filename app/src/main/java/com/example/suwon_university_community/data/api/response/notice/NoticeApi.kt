package com.example.suwon_university_community.data.api.response.notice

import retrofit2.Response
import retrofit2.http.GET

interface NoticeApi {
    @GET("/v0/b/universitysuwoncommunity.appspot.com/o/notice.json?alt=media")
    suspend fun getNoticeList() : Response<NoticeResponse>
}