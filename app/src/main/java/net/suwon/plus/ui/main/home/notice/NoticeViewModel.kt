package net.suwon.plus.ui.main.home.notice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.R
import net.suwon.plus.data.entity.memo.BookMarkNoticeEntity
import net.suwon.plus.data.entity.notice.NoticeEntity
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.data.repository.notice.NoticeRepository
import net.suwon.plus.model.NoticeDateModel
import net.suwon.plus.model.NoticeModel
import net.suwon.plus.ui.base.BaseViewModel
import net.suwon.plus.util.provider.ResourceProvider
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

            val noticeDateModel = toNoticeDateModel(noticeModelList)

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
            date = "${date.first}-${date.second}-${date.third}",
            category = resourceProvider.getString(noticeModel.category.categoryNameId) ,
            url = noticeModel.url,
            noticeFolderId = 1
        )
        )
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