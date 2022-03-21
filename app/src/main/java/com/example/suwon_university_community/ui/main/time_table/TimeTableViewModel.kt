package com.example.suwon_university_community.ui.main.time_table

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.timetable.TimeTableEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableWithCell
import com.example.suwon_university_community.data.preference.PreferenceManager
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
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


/**
 *
 * 테이블 저장 할떄 .
 * 테이블 리스트를 가지고 있어서 해당 이름과 같은지 확인해줘야한다. 있으면 toast
 *
 *
 * 테이블 리스트를 가지고 있어야한다.
 *
 * default 인지 boolean값도 저장해준다 (기본 시간표인지)
 *
 *
 *
 * Main 시간표 id를 Preference에 저장해준다.  그 시간표를 메인화면 시간표로 설정해준다.
 *
 *
 * 메인 화면에서는 리스를 가져와서 empty인 경우 비어있다고 보여주고
 * 리스트가 있는데 메인 시간표로 설정이 안될수도 있지 혹시모르니까 그런경우 리스트로 가서
 * 메인 리스트로 설정해주기
 *
 *
 */


class TimeTableViewModel @Inject constructor(
    private val timeTableRepository: TimeTableRepository,
    private val preferenceManager: PreferenceManager
) : BaseViewModel() {

    val timeTableStateLiveData = MutableLiveData<TimeTableState>(TimeTableState.Uninitialized)

    lateinit var mainTimeTable: TimeTableWithCell

    override fun fetchData(): Job = viewModelScope.launch {
        timeTableStateLiveData.value = TimeTableState.Loading

        val mainTimeTableId = preferenceManager.getMainTimeTableId()

        mainTimeTableId?.let {
            mainTimeTable = timeTableRepository.getTimeTableWithCell(preferenceManager.getMainTimeTableId()!!)

//            timeTableRepository.insertTimeTableCellWithTable(mainTimeTableId    ,
//            TimeTableCellEntity(
//                cellId = 0,
//                name = "강의 이름",
//                distinguish = "ㅇ",
//                grade = 1,
//                locationAndTimeList = listOf(
//                    TimeTableLocationAndTime("종강B102", "월" , listOf(1,2,3))
//                ),
//                professorName = "교수이름",
//            )
//            )


            timeTableStateLiveData.value = TimeTableState.Success(mainTimeTable)
        } ?: kotlin.run {
            timeTableStateLiveData.value = TimeTableState.NoTable
        }

    }


    fun saveNewTimeTable(tableName: String, year: Int, semester: Int) = viewModelScope.launch {

        val tableId = System.currentTimeMillis()

        timeTableRepository.insertTimeTable(
            TimeTableEntity(
                tableId = tableId,
                tableName = tableName,
                year = year,
                semester = semester,
                isDefault = true
            )
        )

        preferenceManager.putMainTimeTableId(tableId)

        fetchData()
    }

    fun updateTimeTableEntity() {

    }

    fun deleteTImeTableEntity() {

    }


}