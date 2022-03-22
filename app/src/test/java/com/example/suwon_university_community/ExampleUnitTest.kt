package com.example.suwon_university_community

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    fun addition_isCorrect() {
        "인문211(월1 토1,2),자연대506(월:5,6)"

        val splite = "인문211(월1/4/6 토1/2),자연대506(월5/6)".split(",")

        splite.forEach {
            System.out.println(it)
        }

        var sumString = ""


        for (i in 0 until splite.size - 1) {
            sumString = "${sumString}${splite[i]},"
        }

        sumString += splite[splite.size - 1]

        System.out.println(sumString)
    }

}