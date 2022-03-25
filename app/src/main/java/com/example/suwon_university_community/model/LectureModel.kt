package com.example.suwon_university_community.model

import androidx.room.Entity
import com.example.suwon_university_community.data.entity.timetable.TimeTableLocationAndTime


@Entity
data class LectureModel(
    override val id: Long,
    val cellType: CellType = CellType.LECTURE_CELL,
    val name: String?,
    val distinguish: String?,
    val point: Float?,
    val time: String?,
    val department: String?,
    val major: String?,
    val professorName: String?
) : Model(id, cellType){

    //인문211(월1,4,6 토1,2),인문211(월1,4,6 토1,2)
    fun toTimeTableCellModel(): TimeTableCellModel {

        val spl = this.time!!.split("),")


        val locationAndTimeList = arrayListOf<TimeTableLocationAndTime>()


        spl.forEach { it ->


            val temp = it.split("(")
            val location = temp[0]

            val dayAndTime = temp[1].split(" ")

            dayAndTime.forEach { dayAndTime ->      // 월1/4/5)
                val day = dayAndTime[0]

                val timeArray = dayAndTime.substring(1, dayAndTime.lastIndex).split(',').map {
                    it.toInt()
                }


                locationAndTimeList.add(
                    TimeTableLocationAndTime(location = location, day = day, time = timeArray)
                )
            }

        }


        return TimeTableCellModel(
            id = id,
            name = name ?: "",
            locationAndTimeList = locationAndTimeList,
            professorName = professorName ?: "",
        )
    }
}