package com.example.suwon_university_community.data.api.response.notice


import com.google.gson.annotations.SerializedName

data class NoticeItem(
    @SerializedName("date")
    var date: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("urlNumber")
    var urlNumber: String?,
    @SerializedName("writer")
    var writer: String?,
    @SerializedName("type")
    var type: String?
)