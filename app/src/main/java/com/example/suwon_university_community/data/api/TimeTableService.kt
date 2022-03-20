package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.entity.lecture.LectureEntity


interface TimeTableService{

    suspend fun getUpdatedTimeMillis() : Long

    suspend fun getTimeTableList() : List<LectureEntity>

}