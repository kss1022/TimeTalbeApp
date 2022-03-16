package com.example.suwon_university_community.ui.main.time_table

import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTableViewModel  @Inject constructor() : BaseViewModel() {

    override fun fetchData(): Job = viewModelScope.launch{
    }
}