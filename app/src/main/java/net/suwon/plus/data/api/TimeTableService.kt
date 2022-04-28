package net.suwon.plus.data.api

import net.suwon.plus.data.entity.lecture.LectureEntity


interface TimeTableService{

    suspend fun getUpdatedTimeMillis() : Long

    suspend fun getTimeTableList() : List<LectureEntity>

}