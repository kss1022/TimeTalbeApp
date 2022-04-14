package com.example.suwon_university_community.ui.main.memo.folder.bookmark

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.model.NoticeDateModel
import com.example.suwon_university_community.model.NoticeModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookmarkListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    var folderId = 0L
    val bookmarkListStateLiveData = MutableLiveData<BookmarkListState>(BookmarkListState.Uninitialized)

    override fun fetchData(): Job  = viewModelScope.launch{

        bookmarkListStateLiveData.value = BookmarkListState.Loading
        val noticeEntityList =  memoRepository.getFolderWithNotice(folderId).notices.map {
            it.toModel()
        }

        val noticeDateModel = toNoticeDateModel(noticeEntityList)


        bookmarkListStateLiveData.value = BookmarkListState.Success(noticeDateModel)
    }




    private fun toNoticeDateModel(noticeModelList: List<NoticeModel>): MutableList<NoticeDateModel> {
        val noticeDateModel = mutableListOf<NoticeDateModel>()


        val dateSet = mutableSetOf<Triple<Int, Int, Int>>()
        noticeModelList.forEach {
            dateSet.add(it.date)
        }

        val sortedData = dateSet.sortedWith(
            compareByDescending<Triple<Int, Int, Int>> { it.first }
                .thenByDescending { it.second }
                .thenByDescending { it.third }
        )

        sortedData.forEach { sortedDate ->
            val noticeList = mutableListOf<NoticeModel>()

            noticeModelList.forEach { noticeDate ->
                if (sortedDate == noticeDate.date) {
                    noticeList.add(noticeDate)
                }
            }

            noticeDateModel.add(
                NoticeDateModel(
                    sortedDate,
                    noticeList.sortedBy { it.category })
            )
        }
        return noticeDateModel
    }
}