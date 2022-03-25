package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.api.response.LectureApi
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class JsonTimeTableService @Inject constructor(
    private val firebaseStore: FirebaseStorage,
    private val lectureApi: LectureApi
) : TimeTableService {

    private val sheetReference: StorageReference =
        firebaseStore.reference.child(LECTURE_FILE_NAME)


    override suspend fun getUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getTimeTableList(): List<LectureEntity> =
        try {
            val response = lectureApi.getLectureList()


            if (response.isSuccessful) {
                response.body()?.map {
                    LectureEntity(
                        name = it.estbSubjtNm,
                        distinguish = it.facDvnm,
                        time = it.timtSmryCn,
                        point = it.point,
                        department = it.estbDpmjNm,
                        major = it.stafDeptNm,
                        grade = it.trgtGrdeNm,
                        professorName = it.stafNm
                    )
                } ?: listOf()
            } else {
                listOf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }


    companion object {
        private const val LECTURE_FILE_NAME = "2022.01.json"
    }
}