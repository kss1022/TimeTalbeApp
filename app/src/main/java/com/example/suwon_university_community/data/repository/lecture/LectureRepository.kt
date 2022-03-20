package com.example.suwon_university_community.data.repository.lecture

//Update LectureList
interface LectureRepository {

    suspend fun refreshLecture()
}