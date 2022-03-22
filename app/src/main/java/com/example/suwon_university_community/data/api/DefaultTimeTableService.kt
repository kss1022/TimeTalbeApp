package com.example.suwon_university_community.data.api

import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.DepartmentCategory
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class DefaultTimeTableService @Inject constructor(
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


                    var locationAndTime = it[3]

                    val locationNum = it.size - CELL_COUNT

                    when(locationNum){
                        1->{ locationAndTime =locationAndTime +","+ it[4]}
                        2->{locationAndTime = locationAndTime + "," +it[4] + "," + it[5] }
                        3->{locationAndTime = locationAndTime + ","+ it[4] + ","+  it[5] +"," + it[6]}
                    }


                    LectureEntity(
                        name = it[0],
                        distinguish = it[1],
                        grade = it[2].toInt(),
                        time = locationAndTime,
                        collegeCategory = CollegeCategory.ICT,
                        departmentCategory = it[it.size - 2].toIctDepartment(),
                        professorName = it[it.size - 1],
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
        private const val CELL_COUNT = 6
    }
}