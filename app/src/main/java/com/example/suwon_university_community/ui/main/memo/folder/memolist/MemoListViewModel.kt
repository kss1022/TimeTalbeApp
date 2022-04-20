package com.example.suwon_university_community.ui.main.memo.folder.memolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.model.MemoModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    var folderId = 0L

    val memoListStateLiveData = MutableLiveData<MemoListState>(MemoListState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        memoListStateLiveData.value = MemoListState.Loading


        val memoList = memoRepository.getFolderWithMemo(folderId).memos.map { memo ->
            memo.toModel()
        }.sortedByDescending { it.time }

        memoListStateLiveData.value = MemoListState.Success(memoList)
    }

    fun deleteMemo(model: MemoModel) = viewModelScope.launch {
        memoRepository.deleteMemo(model)

        when (val data = memoListStateLiveData.value) {
            is MemoListState.Success -> {
                memoListStateLiveData.value = data.copy(
                    data.memoList.toMutableList().apply {
                        remove(model)
                    }
                )
            }
        }
    }

    fun changeFolder(model: MemoModel, folderId: Long) = viewModelScope.launch {
        memoRepository.changeFolder( model , folderId)

        when (val data = memoListStateLiveData.value) {
            is MemoListState.Success -> {
                memoListStateLiveData.value = data.copy(
                    data.memoList.toMutableList().apply {
                        remove(model)
                    }
                )
            }
        }
    }

}