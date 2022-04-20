package com.example.suwon_university_community.data.entity.notice

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.suwon_university_community.model.NoticeModel
import com.example.suwon_university_community.ui.main.home.notice.NoticeCategory

@Entity
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val writer: String,
    val date: String,
    val category: String,
    val url: String
) {
    fun toModel() = NoticeModel(
        id = id,
        title = title,
        writer = writer,
        date = date.toDate(),
        category = category.toCategory(),
        url = url
    )


    private fun String?.toCategory(): NoticeCategory =
        when (this) {
            "통합공지" -> NoticeCategory.INTEGRATION
            "학과공지" -> NoticeCategory.DEPARTMENT
            "취업공지" -> NoticeCategory.EMPLOYMENT
            "창업공지" -> NoticeCategory.STARTUP
            "국제협력처공지" -> NoticeCategory.INTERNATIONAL_COOPERATION
            "어학원공지" -> NoticeCategory.LANGUAGE_SCHOOL
            "장학/대출" -> NoticeCategory.SCHOLARSHIP_AND_LOAN
            else -> NoticeCategory.ALL
        }


    private fun String.toDate(): Triple<Int, Int, Int> {

        val split = this.split("-")
        //2021-11-23


        return Triple(split[0].toInt(), split[1].toInt(), split[2].toInt())
    }

}

