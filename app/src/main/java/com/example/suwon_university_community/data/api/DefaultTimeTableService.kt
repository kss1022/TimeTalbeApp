package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.DepartmentCategory
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DefaultTimeTableService  @Inject constructor(
    private val firebaseStore: FirebaseStorage
) : TimeTableService {

    private val sheetReference: StorageReference =
        firebaseStore.reference.child(ICT)


    override suspend fun getUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getTimeTableList(): List<LectureEntity> {

        try {
            val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
            val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

            return byteArray.decodeToString()
                .lines()
                .drop(1)
                .map { it.split(",") }
                .map {
                    LectureEntity(
                        id = hashCode().toLong(),
                        name = it[0],
                        distinguish = it[1],
                        grade = it[2].toInt(),
                        time = it[3],
                        collegeCategory = CollegeCategory.ICT,
                        department = it[4].toIctDepartment(),
                        professorName = it[5],
                    )
                }
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }


    private fun String.toIctDepartment(): DepartmentCategory? {
        return when (this) {

            "데이터과학부" -> {
                DepartmentCategory.DS
            }
            "클라우드융복합전공" -> {
                DepartmentCategory.CLOUD_CONVERGENCE
            }
            "컴퓨터학부" -> {
                DepartmentCategory.COM
            }
            "컴퓨터학부>>컴퓨터SW" -> {
                DepartmentCategory.COMPUTER_SW
            }
            "컴퓨터학부>>미디어SW" -> {
                DepartmentCategory.MEDIA_SW
            }

            "정보통신학부" -> {
                DepartmentCategory.IF_CM
            }

            "정보통신학부>>정보통신" -> {
                DepartmentCategory.IF_CM_COMMUNICATION
            }
            "정보통신학부>>정보보호" -> {
                DepartmentCategory.IF_CM_SECURE
            }
            else -> null
        }
    }


    companion object {
        private const val ICT = "ict_data.csv"
    }
}