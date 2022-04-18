package com.example.suwon_university_community.ui.main.memo.folder.editmemo

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.memo.MemoEntity
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditMemoViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : BaseViewModel() {

    override fun fetchData(): Job  = viewModelScope.launch {  }

    fun insertMemo(memoEntity: MemoEntity) = viewModelScope.launch {
        memoRepository.insertMemo(memoEntity)
    }


    fun updateMemo( memoEntity: MemoEntity) = viewModelScope.launch {
        memoRepository.updateMemo(memoEntity)
    }
}