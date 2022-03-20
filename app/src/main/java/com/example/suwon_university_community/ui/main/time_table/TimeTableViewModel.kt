package com.example.suwon_university_community.ui.main.time_table

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *
 * 시간표 생성!
 * Base 학기 시간표 : 년도  = 2022, 학기 = 1. 이름 = nullable
 *
 *  추가할 fragment :  시간표 리스트(추가 삭제 이름 변경 base 설정)
 *
 */

class TimeTableViewModel  @Inject constructor(
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val timeTableStateLiveData = MutableLiveData<TimeTableState>(TimeTableState.Uninitialized)

    override fun fetchData(): Job = viewModelScope.launch{
        timeTableStateLiveData.value =  TimeTableState.Loading


        timeTableStateLiveData.value =  TimeTableState.NoTable
    }

    fun saveNewTimeTable(){

    }

    fun updateTimeTableEntity(){

    }

    fun deleteTImeTableEntity(){

    }


}