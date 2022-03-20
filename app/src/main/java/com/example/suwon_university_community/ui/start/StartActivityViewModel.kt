package com.example.suwon_university_community.ui.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.repository.lecture.LectureRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class StartActivityViewModel @Inject constructor(
    private val lectureRepository: LectureRepository
) : BaseViewModel() {

    val startStateLiveData = MutableLiveData<StartState>(StartState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch {
        startStateLiveData.value = StartState.Loading
        lectureRepository.refreshLecture()
        startStateLiveData.value = StartState.Success
    }
}