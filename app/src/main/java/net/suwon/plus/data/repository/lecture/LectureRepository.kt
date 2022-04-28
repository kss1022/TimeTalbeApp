package net.suwon.plus.data.repository.lecture

import net.suwon.plus.data.entity.lecture.LectureEntity

//Update LectureList
interface LectureRepository {

    suspend fun refreshLecture()

    suspend fun getLectureList(department: String) : List<LectureEntity>
}