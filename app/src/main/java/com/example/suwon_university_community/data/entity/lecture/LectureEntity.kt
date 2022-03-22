package com.example.suwon_university_community.data.entity.lecture

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime
import com.example.suwon_university_community.model.CellType
import com.example.suwon_university_community.model.LectureModel


@Entity
data class LectureEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val distinguish: String?,
    val grade: Int?,
    val time: String?,
    val collegeCategory: CollegeCategory?,
    val departmentCategory: DepartmentCategory?,
    val professorName: String?
) {


    fun toLectureModel() = LectureModel(
        id = id,
        cellType = CellType.LECTURE_CELL,
        name = name,
        distinguish = distinguish,
        grade = grade,
        time = time,
        collegeCategory = collegeCategory,
        departmentCategory = departmentCategory,
        professorName = professorName
    )


    // TODO: 변경하기
    //    time = "인문211(월1 토1,2)",
    //    time = "인문211(월1/4/6 토1/2),자연대506(월5/6)"
    //
    fun toTimeTableCellEntity(): TimeTableCellEntity {

        val spl = this.time!!.split(",")


        val locationAndTimeList = arrayListOf<TimeTableLocationAndTime>()


        spl.forEach { it ->

            //인문211(월1/4/6 토1/2)
            val temp = it.split("(")
            val location = temp[0]

            val dayAndTime = temp[1].split(" ")

            dayAndTime.forEach { dayAndTime ->      // 월1/4/5
                val day = dayAndTime[0]
                val timeArray = dayAndTime.substring(1).split('/').map { it.toInt() }


                locationAndTimeList.add(
                    TimeTableLocationAndTime(location = location, day = day, time = timeArray)
                )
            }

        }


        return TimeTableCellEntity(
            cellId = id,
            name = name ?: "",
            distinguish = distinguish ?: "",
            grade = grade ?: 0,
            locationAndTimeList = locationAndTimeList,
            professorName = professorName ?: "",
        )


//
//        val temp = this.time!!.split("(")
//
//        val location = temp[0]  //인문211
//        val locationAndTime: ArrayList<TimeTableLocationAndTime> = arrayListOf()
//
//
//        val dayAndTime = temp[1]
//
//        if (dayAndTime.contains(" ").not()) {
//            //            수:3,4,5)
//            val split = dayAndTime.split(':', ',', ')')
//
//            val day = split[0]
//            val time: ArrayList<Int> = arrayListOf()
//
//            for (i in 1..split.size - 2) {
//                time.add(split[i].toInt())
//            }
//
//
//            locationAndTime.add(TimeTableLocationAndTime(location, day, time))
//        } else {
//            //            월:1 토:1,2)
//            val split = dayAndTime.split(" ")
//
//            //월:1
//            val first = split[0]
//            val fistSplit = first.split(':', ',')
//
//            val firstDay = fistSplit[0]
//            val firstTime: ArrayList<Int> = arrayListOf()
//
//            for (i in 1 until fistSplit.size) {
//                firstTime.add(fistSplit[i].toInt())
//            }
//
//
//            //토:1,2)
//            val second = split[1]
//            val secondSplit = second.split(':', ',', ')')
//
//            val secondDay = secondSplit[0]
//            val secondTime: ArrayList<Int> = arrayListOf()
//
//
//            for (i in 1 until secondSplit.size - 1) {
//                secondTime.add(secondSplit[i].toInt())
//            }
//
//
//            locationAndTime.add(TimeTableLocationAndTime(location, firstDay, firstTime))
//            locationAndTime.add(TimeTableLocationAndTime(location, secondDay, secondTime))
//        }
//
//
//
//        return TimeTableCellEntity(
//            cellId = id,
//            name = name ?: "",
//            distinguish = distinguish ?: "",
//            grade = grade ?: 0,
//            locationAndTimeList = locationAndTime,
//            professorName = professorName ?: "",
//        )
    }
}