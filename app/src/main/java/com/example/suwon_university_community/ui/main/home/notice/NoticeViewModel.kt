package com.example.suwon_university_community.ui.main.home.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.memo.BookMarkNoticeEntity
import com.example.suwon_university_community.data.entity.notice.NoticeEntity
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.data.repository.notice.NoticeRepository
import com.example.suwon_university_community.model.NoticeModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *     날자에 맞게 정렬해줘야한다. -> 날자가 같은것 끼리 하나의 객체로 바꿔줘야한다. (
 *
 */

class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository,
    private val memoRepository: MemoRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    var category: NoticeCategory? = null

    val noticeListStateLiveData = MutableLiveData<NoticeListState>(NoticeListState.Uninitialized)

    private var noticeEntityList: List<NoticeEntity> = listOf()

    override fun fetchData(): Job = viewModelScope.launch {
        noticeListStateLiveData.value = NoticeListState.Loading

        category?.let {
            if (category == NoticeCategory.ALL) {
                noticeEntityList = noticeRepository.getNoticeList(
                    resourceProvider.getString(category!!.categoryNameId)
                )
            }


            noticeEntityList =
                noticeRepository.getNoticeList(resourceProvider.getString(category!!.categoryNameId))


            val noticeModelList = noticeEntityList.map {
                it.toModel()
            }

            val noticeDateModel = mutableListOf<NoticeDateModel>()


            val dateSet = mutableSetOf<Triple<Int, Int, Int>>()
            noticeModelList.forEach {
                dateSet.add(it.date)
            }

            val sortedData = dateSet.sortedWith(
            compareByDescending<Triple<Int, Int, Int>> {  it.first  }
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


            noticeListStateLiveData.value = NoticeListState.Success(noticeDateModel)

        } ?: kotlin.run {
            noticeListStateLiveData.value = NoticeListState.Error(R.string.notice_category_is_null)

        }


    }

    fun saveNotice(noticeModel: NoticeModel) = viewModelScope.launch{
        val date  = noticeModel.date

        memoRepository.insertBookMarkNotice( BookMarkNoticeEntity(

            title = noticeModel.title,
            writer = noticeModel.writer,
            date = "${date.first}년 ${date.second}월 ${date.third}일",
            category = resourceProvider.getString(noticeModel.category.categoryNameId) ,
            url = noticeModel.url,
            noticeFolderId = 1
        ))
    }
}