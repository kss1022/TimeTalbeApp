package com.example.suwon_university_community.data.entity.lecture

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime


@Entity
data class LectureEntity(
    @PrimaryKey val id: Long,
    val name: String?,
    val distinguish: String?,
    val grade: Int?,
    val time: String?,
    val collegeCategory: CollegeCategory?,
    val department: DepartmentCategory?,
    val professorName: String?
) {

    //    time = "글경703(수:3,4,5)",
    //    time = "인문211(월1 토1,2)",

    fun toTimeTableCellEntity(timeTableId: Int): TimeTableCellEntity {
        val temp = this.time!!.split("(")

        val location = temp[0]
        val locationAndTime: ArrayList<TimeTableLocationAndTime> = arrayListOf()


        val dayAndTime = temp[1]

        if (dayAndTime.contains(" ").not()) {
            //            수:3,4,5)
            val split = dayAndTime.split(':', ',', ')')

            val day = split[0]
            val time: ArrayList<Int> = arrayListOf()

            for (i in 1..split.size - 2) {
                time.add(split[i].toInt())
            }


            locationAndTime.add(TimeTableLocationAndTime(location, day, time))
        } else {
            //            월:1 토:1,2)
            val split = dayAndTime.split(" ")

            //월:1
            val first = split[0]
            val fistSplit = first.split(':', ',')

            val firstDay = fistSplit[0]
            val firstTime: ArrayList<Int> = arrayListOf()

            for (i in 1 until fistSplit.size) {
                firstTime.add(fistSplit[i].toInt())
            }


            //토:1,2)
            val second = split[1]
            val secondSplit = second.split(':', ',', ')')

            val secondDay = secondSplit[0]
            val secondTime: ArrayList<Int> = arrayListOf()


            for (i in 1 until secondSplit.size - 1) {
                secondTime.add(secondSplit[i].toInt())
            }


            locationAndTime.add(TimeTableLocationAndTime(location, firstDay, firstTime))
            locationAndTime.add(TimeTableLocationAndTime(location, secondDay, secondTime))
        }



        return TimeTableCellEntity(
            cellId = id,
            name = name ?: "",
            distinguish = distinguish ?: "",
            grade = grade ?: 0,
            locationAndTimeList = locationAndTime,
            professorName = professorName ?: "",
            timeTableId = timeTableId
        )
    }
}