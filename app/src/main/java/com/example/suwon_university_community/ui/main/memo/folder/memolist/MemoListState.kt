package com.example.suwon_university_community.ui.main.memo.folder.memolist

import com.example.suwon_university_community.model.MemoModel

sealed class MemoListState {
    object Uninitialized : MemoListState()

    object Loading : MemoListState()

    data class Success(
        val memoList: List<MemoModel>
    ) : MemoListState()
}