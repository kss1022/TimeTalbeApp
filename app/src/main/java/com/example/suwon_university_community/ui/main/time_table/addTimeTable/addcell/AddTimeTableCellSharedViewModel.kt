package com.example.suwon_university_community.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.suwon_university_community.model.LectureModel

class AddTimeTableCellSharedViewModel(): ViewModel() {
    val lectureEntityLiveData = MutableLiveData<LectureModel>()
}