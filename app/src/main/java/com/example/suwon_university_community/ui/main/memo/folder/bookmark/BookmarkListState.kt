package com.example.suwon_university_community.ui.main.memo.folder.bookmark

sealed class BookmarkListState {
    object Uninitialized : BookmarkListState()

    object Loading : BookmarkListState()

    object Success : BookmarkListState()

}