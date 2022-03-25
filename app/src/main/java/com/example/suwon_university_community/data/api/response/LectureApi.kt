package com.example.suwon_university_community.data.api.response

import retrofit2.Response
import retrofit2.http.GET

interface LectureApi {

    @GET("/v0/b/universitysuwoncommunity.appspot.com/o/2022.01.json?alt=media&token=9a3b01b6-f082-4b04-a3a5-6ef0ff487add")
    suspend fun getLectureList() : Response<LectureResponse>
}