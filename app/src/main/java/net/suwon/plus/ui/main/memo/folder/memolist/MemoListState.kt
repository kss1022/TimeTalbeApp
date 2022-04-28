package net.suwon.plus.ui.main.memo.folder.memolist

import net.suwon.plus.model.MemoModel

sealed class MemoListState {
    object Uninitialized : MemoListState()

    object Loading : MemoListState()

    data class Success(
        val memoList: List<MemoModel>
    ) : MemoListState()
}