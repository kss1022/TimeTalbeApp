package net.suwon.plus.ui.main.memo.folder.bookmark

sealed class BookmarkListState {
    object Uninitialized : BookmarkListState()

    object Loading : BookmarkListState()

    object Success : BookmarkListState()

}