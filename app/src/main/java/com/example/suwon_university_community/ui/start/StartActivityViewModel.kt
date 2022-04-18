package com.example.suwon_university_community.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.memo.FolderCategory
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.repository.lecture.LectureRepository
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.data.repository.notice.NoticeRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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