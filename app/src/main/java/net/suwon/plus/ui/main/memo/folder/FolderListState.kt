package net.suwon.plus.ui.main.memo.folder

sealed class FolderListState {
    object Uninitialized : FolderListState()

    object Loading : FolderListState()

    object Success: FolderListState()

}