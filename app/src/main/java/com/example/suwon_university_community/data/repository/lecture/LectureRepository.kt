package com.example.suwon_university_community.data.repository.lecture

import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.LectureEntity

//Update LectureList
interface LectureRepository {

    suspend fun refreshLecture()

    suspend fun getLectureList(category: CollegeCategory) : List<LectureEntity>
}