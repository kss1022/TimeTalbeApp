package com.example.suwon_university_community.ui.main.home.notice

import androidx.annotation.StringRes
import com.example.suwon_university_community.model.NoticeDateModel

sealed class NoticeListState {

    object Uninitialized : NoticeListState()

    object Loading : NoticeListState()

    data class Success(
        val noticeDateModelList : List<NoticeDateModel>
    ) : NoticeListState()


    data class Error(
        @StringRes val massageId: Int
    ) : NoticeListState()
}