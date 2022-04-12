package com.example.suwon_university_community.ui.main.memo.folder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.memo.FolderEntity
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class FolderListViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    val memoStateLiveData = MutableLiveData<FolderListState>(FolderListState.Uninitialized)
    val folders: MutableStateFlow<List<FolderEntity>> = MutableStateFlow(emptyList())

    override fun fetchData(): Job = viewModelScope.launch{
        memoStateLiveData.value = FolderListState.Loading
        memoRepository.getFolderList().onEach { folders.value = it }.launchIn(this)
        memoStateLiveData.value = FolderListState.Success

    }
}