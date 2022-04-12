package com.example.suwon_university_community.ui.main.memo.folder

sealed class FolderListState {
    object Uninitialized : FolderListState()

    object Loading : FolderListState()

    object Success: FolderListState()

}