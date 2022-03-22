package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell.lecturelist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.suwon_university_community.data.entity.lecture.CollegeCategory
import com.example.suwon_university_community.data.entity.lecture.LectureEntity
import com.example.suwon_university_community.data.repository.lecture.LectureRepository
import com.example.suwon_university_community.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class LectureListViewModel @Inject constructor(
    private val lectureRepository: LectureRepository
): BaseViewModel() {

    var category : CollegeCategory? = null

    val lectureListLiveData = MutableLiveData<List<LectureEntity>>()

    override fun fetchData(): Job = viewModelScope.launch{
        category?.let {
            val list = lectureRepository.getLectureList(category!!)

            lectureListLiveData.value =  lectureRepository.getLectureList(category!!)
        }
    }

}