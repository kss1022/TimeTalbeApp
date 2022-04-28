package net.suwon.plus.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.suwon.plus.data.entity.memo.FolderCategory
import net.suwon.plus.data.entity.memo.FolderEntity
import net.suwon.plus.data.repository.lecture.LectureRepository
import net.suwon.plus.data.repository.memo.MemoRepository
import net.suwon.plus.data.repository.notice.NoticeRepository
import net.suwon.plus.ui.base.BaseViewModel
import javax.inject.Inject

class StartActivityViewModel @Inject constructor(
    private val lectureRepository: LectureRepository,
    private val noticeRepository: NoticeRepository,
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    val startStateLiveData = MutableLiveData<StartState>(StartState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        startStateLiveData.value = StartState.Loading
        lectureRepository.refreshLecture()
        noticeRepository.refreshNotice()

        val folderCount = memoRepository.getFolderCount()
        if(folderCount == null || folderCount== 0){
            val defaultNoticeFolder =  FolderEntity(
                1,
                "북마크",
                category = FolderCategory.NOTICE,
                isDefault = true
            )


//            val defaultTimeTableFolder =  FolderEntity(
//                2,
//                "메모",
//                category = FolderCategory.TIME_TABLE,
//                isDefault = true
//            )

            val defaultBaseMemoFolder =  FolderEntity(
                2,
                "메모",
                category = FolderCategory.MEMO,
                isDefault = true
            )

            memoRepository.insertFolder(defaultNoticeFolder)
//            memoRepository.insertFolder(defaultTimeTableFolder)
            memoRepository.insertFolder(defaultBaseMemoFolder)
        }


        startStateLiveData.value = StartState.Success
    }
}