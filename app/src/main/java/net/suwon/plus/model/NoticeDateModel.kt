package net.suwon.plus.model

data class NoticeDateModel(
    val date: Triple<Int, Int, Int>,
    val noticeModel: List<NoticeModel>
){
    fun getDate()  : String = "${date.first}년${date.second}월${date.third}일"
}
