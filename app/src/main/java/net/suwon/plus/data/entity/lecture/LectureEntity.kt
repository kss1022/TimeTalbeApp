package net.suwon.plus.data.entity.lecture

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.suwon.plus.model.CellType
import net.suwon.plus.model.LectureModel


@Entity
data class LectureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val distinguish: String?,
    val point: Float?,
    val time: String?,
    val department: String?,
    val major: String?,
    val grade: String?,
    val professorName: String?
) {


    fun toLectureModel() = LectureModel(
        id = id,
        cellType = CellType.LECTURE_CELL,
        name = name,
        distinguish = distinguish,
        point = point,
        time = time,
        department = department,
        major = major,
        grade = grade,
        professorName = professorName
    )
}