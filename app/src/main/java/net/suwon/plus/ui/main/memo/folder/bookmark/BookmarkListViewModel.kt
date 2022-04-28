package net.suwon.plus.ui.main.memo.folder.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.model.NoticeModel
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class BookmarkListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    var folderId = 0L
    val bookmarkListStateLiveData = MutableLiveData<BookmarkListState>(BookmarkListState.Uninitialized)
    val bookmarks : MutableStateFlow<List<BookMarkNoticeEntity>> = MutableStateFlow(emptyList())

    override fun fetchData(): Job  = viewModelScope.launch{
        bookmarkListStateLiveData.value = BookmarkListState.Loading
        memoRepository.getBookMarkList().onEach { bookmarks.value = it }.launchIn(this)
        bookmarkListStateLiveData.value = BookmarkListState.Success
    }

    fun deleteBookmark(noticeModel: NoticeModel) = viewModelScope.launch{
            memoRepository.deleteBookMarkNotice(noticeModel.id)
    }
}