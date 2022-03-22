package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTimeTableCellViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository
): BaseViewModel() {


    val addTimeTableStateLiveData = MutableLiveData<AddTimeTableCellState>(AddTimeTableCellState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        addTimeTableStateLiveData.value = AddTimeTableCellState.Loading

        addTimeTableStateLiveData.value = AddTimeTableCellState.Success
    }
}