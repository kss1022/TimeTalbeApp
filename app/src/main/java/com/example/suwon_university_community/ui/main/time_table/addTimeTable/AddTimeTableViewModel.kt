package com.example.suwon_university_community.ui.main.time_table.addTimeTable

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTimeTableViewModel @Inject constructor(
): BaseViewModel() {


    val addTimeTableStateLiveData = MutableLiveData<AddTimeTableState>(AddTimeTableState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        addTimeTableStateLiveData.value = AddTimeTableState.Loading



        addTimeTableStateLiveData.value = AddTimeTableState.Success
    }
}