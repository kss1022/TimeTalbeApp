package com.example.suwon_university_community.model

data class NoticeDateModel(
    val date: Triple<Int, Int, Int>,
    val noticeModel: List<NoticeModel>
){
    fun getDate()  : String = "${date.first}년${date.second}월${date.third}일"
}
