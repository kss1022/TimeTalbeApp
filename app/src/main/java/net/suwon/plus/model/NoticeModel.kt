package net.suwon.plus.model

import net.suwon.plus.ui.main.home.notice.NoticeCategory

data class NoticeModel(
    val id : Long,
    val title: String,
    val writer: String,
    val date: Triple<Int, Int, Int>,
    val category : NoticeCategory,
    val url: String
)