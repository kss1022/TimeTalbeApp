package com.example.suwon_university_community.ui.main.memo.folder.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.model.NoticeModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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