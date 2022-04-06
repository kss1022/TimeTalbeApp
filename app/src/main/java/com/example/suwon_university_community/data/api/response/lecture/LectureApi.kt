package com.example.suwon_university_community.data.api.response.lecture

import retrofit2.Response
import retrofit2.http.GET

interface LectureApi {

    @GET("/v0/b/universitysuwoncommunity.appspot.com/o/2022.01.json?alt=media")
    suspend fun getLectureList() : Response<LectureResponse>
}