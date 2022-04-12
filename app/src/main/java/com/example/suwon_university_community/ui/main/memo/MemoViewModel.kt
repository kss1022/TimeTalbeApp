package com.example.suwon_university_community.ui.main.memo

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.repository.memo.MemoRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MemoViewModel  @Inject constructor(
) : BaseViewModel() {


    override fun fetchData(): Job = viewModelScope.launch{}
}