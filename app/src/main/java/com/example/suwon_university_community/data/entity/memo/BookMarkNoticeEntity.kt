package com.example.suwon_university_community.data.entity.memo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookMarkNoticeEntity(
    @PrimaryKey(autoGenerate = true) val noticeId : Long = 0,
    val title: String,
    val writer: String,
    val date: String,
    val category: String,
    val url: String,
    val memo : String = "",
    val noticeFolderId : Long
)

