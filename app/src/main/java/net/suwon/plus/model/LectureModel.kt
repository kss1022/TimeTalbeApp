package net.suwon.plus.model

import net.suwon.plus.data.entity.timetable.TimeTableLocationAndTime
import net.suwon.plus.data.entity.timetable.toDayOfTheWeek


data class LectureModel(
    override val id: Long,
    val cellType: CellType = CellType.LECTURE_CELL,
    val name: String?,
    val distinguish: String?,
    val point: Float?,
    val time: String?,
    val department: String?,
    val major: String?,
    val grade: String?,
    val professorName: String?
) : Model(id, cellType) {

    //인문211(월1,4,6 토1,2),인문211(월1,4,6 토1,2)
    fun toTimeTableCellModel(): TimeTableCellModel {
        if (time.isNullOrEmpty()) {
            return TimeTableCellModel(
                id = id,
                name = name ?: "",
                locationAndTimeList = listOf(),
                professorName = professorName ?: "",
            )
        }

        val spl = this.time.split("),")


        val locationAndTimeList = arrayListOf<TimeTableLocationAndTime>()


        spl.forEach { it ->
            //미래106(금5,6),미래102(화2,4) -> 금(5,6      //미래105(금7,8)-> 금(7,8)

            val temp = it.split("(")
            val location = temp[0]

            val dayAndTime = temp[1].split(" ")

            dayAndTime.forEach { dayWithTime ->      // 월1,4,5)
                val day = dayWithTime[0]

                val timeArray: List<Int> = if (dayWithTime.last() == ')') {
                    dayWithTime.substring(1, dayWithTime.lastIndex).split(',').map {
                        it.toInt()
                    }
                } else {
                    dayWithTime.substring(1, dayWithTime.lastIndex + 1).split(',').map {
                        it.toInt()
                    }
                }


                //1교시 -> 9 to 30      10 to 20
                val realTime = if (timeArray.size == 1) {
                    ((timeArray.first() + 8) * 60 + 30) to ((timeArray.first() + 9) * 60 + 20)
                } else {
                    ((timeArray.first() + 8) * 60 + 30) to ((timeArray.last() + 9) * 60 + 20)
                }


                locationAndTimeList.add(
                    TimeTableLocationAndTime(location = location, day = day.toDayOfTheWeek(), time = realTime)
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