package com.example.suwon_university_community.model

import com.example.suwon_university_community.ui.main.home.notice.NoticeCategory

data class NoticeModel(
    val title: String,
    val writer: String,
    val date: Triple<Int, Int, Int>,
    val category : NoticeCategory,
    val url: String
)