package com.example.suwon_university_community.model

import androidx.room.Entity
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.DepartmentCategory


@Entity
data class LectureModel(
    override val id: Long,
    val cellType: CellType = CellType.LECTURE_CELL,
    val name: String?,
    val distinguish: String?,
    val grade: Int?,
    val time: String?,
    val collegeCategory: CollegeCategory?,
    val departmentCategory: DepartmentCategory?,
    val professorName: String?
) : Model(id, cellType)