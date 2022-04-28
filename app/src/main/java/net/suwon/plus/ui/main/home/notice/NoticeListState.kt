package net.suwon.plus.ui.main.home.notice

import androidx.annotation.StringRes
import net.suwon.plus.model.NoticeDateModel

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