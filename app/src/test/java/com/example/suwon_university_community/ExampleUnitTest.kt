package com.example.suwon_university_community

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


//    dayAndTime.forEach { dayAndTime ->      // 월1,4,5)
//        val day = dayAndTime[0]
//
//        val timeArray = dayAndTime.substring(1, dayAndTime.lastIndex).split(',').map {
//            it.toInt()
//        }
//
//
//        locationAndTimeList.add(
//            TimeTableLocationAndTime(location = location, day = day, time = timeArray)
//        )
//    }

    @Test
    fun addition_isCorrect() {
        val dayAndTime = "수1,2"

        System.out.println( dayAndTime.lastIndex )

//        val timeArray = dayAndTime.substring(1, dayAndTime.lastIndex).split(',').map {
//            it.toInt()
//        }



    }


}