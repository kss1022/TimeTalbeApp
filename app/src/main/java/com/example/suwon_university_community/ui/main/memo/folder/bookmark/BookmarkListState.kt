package com.example.suwon_university_community.ui.main.memo.folder.bookmark

import com.example.suwon_university_community.model.NoticeDateModel

sealed class BookmarkListState {
    object Uninitialized : BookmarkListState()

    object Loading : BookmarkListState()

    data class Success(
        val noticeList : List<NoticeDateModel>
    ): BookmarkListState()

}