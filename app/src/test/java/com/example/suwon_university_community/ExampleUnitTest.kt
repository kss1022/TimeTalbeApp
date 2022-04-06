package com.example.suwon_university_community

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {



    @Test
    fun addition_isCorrect() {



        val dataSet = mutableListOf<Triple<Int , Int, Int>>()

        for(i in 1..33){
            dataSet.add(Triple( (1..30).random() , (1..30).random() ,(1..30).random()))
        }


      val sortedData =  dataSet.sortedWith(
            compareByDescending<Triple<Int, Int, Int>> {  it.first  }
                .thenByDescending { it.second }
                .thenByDescending { it.third }
        )

        for(i in sortedData){
            System.out.println(i)
        }

    }


}