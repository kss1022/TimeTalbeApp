package com.example.suwon_university_community

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Test
    fun addition_isCorrect() {

        val time = System.currentTimeMillis()
        val date= Date(time)

        val simpleDateFormatDay = SimpleDateFormat("yyyy-MM-dd")
        val simpleDateFormatTime = SimpleDateFormat("HH:mm:ss")

        val getDay = simpleDateFormatDay.format(date)
        val getTime: String = simpleDateFormatTime.format(date)

        System.out.println(date.toString())
        System.out.println(getDay)
        System.out.println(getTime)
    }


}