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
    val grade : String?,
    val professorName: String?
) : Model(id, cellType){

    //인문211(월1,4,6 토1,2),인문211(월1,4,6 토1,2)
    fun toTimeTableCellModel(): TimeTableCellModel {
        if(time.isNullOrEmpty()){
            return TimeTableCellModel(
                id = id,
                name = name ?: "",
                locationAndTimeList = listOf(),
                professorName = professorName ?: "",
            )
        }

        val spl = this.time!!.split("),")


        val locationAndTimeList = arrayListOf<TimeTableLocationAndTime>()


        spl.forEach { it ->
            //미래106(금5,6),미래102(화2,4) -> 금(5,6      //미래105(금7,8)-> 금(7,8)

            val temp = it.split("(")
            val location = temp[0]

            val dayAndTime = temp[1].split(" ")

            dayAndTime.forEach { dayAndTime ->      // 월1,4,5)
                val day = dayAndTime[0]

                val timeArray : List<Int> =  if(dayAndTime.last() == ')'){
                    dayAndTime.substring(1, dayAndTime.lastIndex).split(',').map {
                        it.toInt()
                    }
                }else{
                    dayAndTime.substring(1, dayAndTime.lastIndex+1).split(',').map {
                        it.toInt()
                    }
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