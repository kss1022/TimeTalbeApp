package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.R
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.example.suwon_university_community.data.entity.timetable.TimeTableCellEntity
import com.example.suwon_university_community.data.repository.lecture.LectureRepository
import com.example.suwon_university_community.data.repository.timetable.TimeTableRepository
import com.example.suwon_university_community.model.LectureModel
import com.example.suwon_university_community.ui.base.BaseViewModel
import com.example.suwon_university_community.util.provider.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class LectureListViewModel @Inject constructor(
    private val lectureRepository: LectureRepository,
    private val timeTableRepository: TimeTableRepository,
    private val resourceProvider: ResourceProvider
) : BaseViewModel() {

    var category: CollegeCategory? = null

    private var lectureEntityList: List<LectureEntity> = listOf()


    val lectureListStateLiveData = MutableLiveData<LectureListState>(LectureListState.Uninitialized)
    val lectureListLiveData = MutableLiveData<List<LectureEntity>>()


    override fun fetchData(): Job = viewModelScope.launch {
        lectureListStateLiveData.value = LectureListState.Loading
        category?.let {
            if (category == CollegeCategory.ALL) {
                category?.categoryTypeList?.forEach {

                    lectureEntityList = lectureEntityList + lectureRepository.getLectureList(
                        resourceProvider.getString(it.first)
                    )
                }
            }

            for (i in 1 until category!!.categoryTypeList.size) {

                lectureEntityList = lectureEntityList + lectureRepository.getLectureList(
                    resourceProvider.getString(category!!.categoryTypeList[i].first)
                )
            }

            lectureListLiveData.value = lectureEntityList
            lectureListStateLiveData.value = LectureListState.Success
        } ?: kotlin.run {
            lectureListStateLiveData.value = LectureListState.Error(R.string.category_is_null)
        }

    }


     fun checkTimeTableAndAdd(currentTableId: Long, model: LectureModel) = viewModelScope.launch {
        val addedList = timeTableRepository.getTimeTableWithCell(currentTableId).timeTableCellList

        val overlappingSet = mutableSetOf<TimeTableCellEntity>()


        model.toTimeTableCellModel().locationAndTimeList.forEach { selected ->
            addedList.forEach { added ->
                added.locationAndTimeList.forEach { addedLocationAndTime ->
                    if (selected.day == addedLocationAndTime.day) {

                        if( (addedLocationAndTime.time.first > selected.time.second ||
                                addedLocationAndTime.time.second < selected.time.first ).not()){
                         overlappingSet.add(added)
                        }
                    }
                }
            }
        }


        if (overlappingSet.isNotEmpty()) {

            var addedString = ""
            overlappingSet.forEach {
                addedString += it.name
                it.locationAndTimeList.forEach { addedString += "  ${it.day} ${it.getTimeString()}" }
            }

            lectureListStateLiveData.value =
                LectureListState.Added(addedString)
        } else {
            lectureListStateLiveData.value = LectureListState.NotAdded(model)
        }
    }


    /**
     * [setSearchString]     키보드 입력한 경우 - [checkSpinner] 하여 현재 입력한 Spinner 리스트를 가져옴 그 리스트에서 Search
     * [searchString]        EditText 에 입력되 있는 값을 찾는다.
     *
     * [setCheckSpinner]     Spinner 가 변경된 경우 [searchString] 하여 입력되 있는 값에서 Filter
     * [checkSpinner]        Spinner 에 입력되 있는 값을 찾아서 Spinner 리스트에 저장해준다.
     */


    private var search = ""
    private var department: String? = null
    private var major: String? = null
    private var grade: String? = null
    private var pos: Int = -1


    fun setSearchString(search: String) {
        this.search = search


        if (search.isBlank()) {
            lectureListLiveData.value = checkSpinner()
        } else {
            lectureListLiveData.value = checkSpinner().filter {
                it.name?.contains(search)!!
            }
        }
    }


    private fun searchString() {
        if (search.isBlank()) {
            lectureListLiveData.value = lectureEntityList
        } else {
            lectureListLiveData.value = lectureEntityList.filter {
                it.name?.contains(search)!!
            }
        }
    }


    fun setDepartmentString(spinner: String) {

        department = if (spinner == "전체") {
            null
        } else {
            spinner
        }

    }


    fun setMajorString(spinner: String, pos: Int) {
        this.pos = pos

        major = if (spinner == "전체") {
            null
        } else {
            spinner
        }

        setCheckSpinner()
    }


    fun setGradeString(spinner: String) {
        grade = if (spinner == "전체") {
            null
        } else {
            spinner
        }

        setCheckSpinner()
    }


    //pos -> 0번쨰인경우 grade 를 Check 하지 않는다.

    private fun setCheckSpinner() {
        searchString()

        if (department != null) {

            if (major != null) {
                if (grade != null) {
                    //1

                    if (pos == 0) {
                        lectureListLiveData.value = lectureListLiveData.value?.filter {
                            it.department == department && it.grade == grade
                        }
                        return
                    }

                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.department == department && it.major == major && it.grade == grade
                    }
                } else {
                    //2

                    if (pos == 0) {
                        lectureListLiveData.value = lectureListLiveData.value?.filter {
                            it.department == department
                        }
                        return
                    }

                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.department == department && it.major == major
                    }
                }
            } else {
                if (grade != null) {
                    //3
                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.department == department && it.grade == grade
                    }
                } else {
                    //4
                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.department == department
                    }
                }
            }
        } else {
            if (major != null) {
                if (grade != null) {
                    //5

                    if (pos == 0) {
                        lectureListLiveData.value = lectureListLiveData.value?.filter {
                            it.grade == grade
                        }
                        return
                    }

                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.major == major && it.grade == grade
                    }
                } else {
                    //6


                    if (pos == 0) {
                        lectureListLiveData.value = lectureListLiveData.value
                        return
                    }

                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.major == major
                    }
                }
            } else {
                if (grade != null) {
                    //7
                    lectureListLiveData.value = lectureListLiveData.value?.filter {
                        it.grade == grade
                    }
                } else {
                    //8
                    lectureListLiveData.value = lectureListLiveData.value
                }
            }
        }
    }


    private fun checkSpinner(): List<LectureEntity> {

        var spinnerList = listOf<LectureEntity>()

        if (department != null) {

            if (major != null) {
                if (grade != null) {
                    //1

                    if (pos == 0) {
                        spinnerList = lectureEntityList.filter {
                            it.department == department && it.grade == grade
                        }

                        lectureListLiveData.value = spinnerList
                        return spinnerList
                    }


                    spinnerList = lectureEntityList.filter {
                        it.department == department && it.major == major && it.grade == grade
                    }
                } else {
                    //2

                    if (pos == 0) {
                        spinnerList = lectureEntityList.filter {
                            it.department == department
                        }
                        lectureListLiveData.value = spinnerList
                        return spinnerList
                    }

                    spinnerList = lectureEntityList.filter {
                        it.department == department && it.major == major
                    }
                }
            } else {
                if (grade != null) {
                    //3
                    spinnerList = lectureEntityList.filter {
                        it.department == department && it.grade == grade
                    }
                } else {
                    //4
                    spinnerList = lectureEntityList.filter {
                        it.department == department
                    }
                }
            }
        } else {
            if (major != null) {
                if (grade != null) {
                    //5

                    if (pos == 0) {
                        spinnerList = lectureEntityList.filter {
                            it.grade == grade
                        }
                        lectureListLiveData.value = spinnerList
                        return spinnerList
                    }

                    spinnerList = lectureEntityList.filter {
                        it.major == major && it.grade == grade
                    }
                } else {
                    //6


                    if (pos == 0) {
                        spinnerList = lectureEntityList
                        lectureListLiveData.value = spinnerList
                        return spinnerList
                    }

                    spinnerList = lectureEntityList.filter {
                        it.major == major
                    }
                }
            } else {
                if (grade != null) {
                    //7
                    spinnerList = lectureEntityList.filter {
                        it.grade == grade
                    }
                } else {
                    //8
                    spinnerList = lectureEntityList
                }
            }
        }


        lectureListLiveData.value = spinnerList
        return spinnerList
    }


}