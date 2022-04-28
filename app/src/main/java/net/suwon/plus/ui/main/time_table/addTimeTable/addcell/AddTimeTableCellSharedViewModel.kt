package net.suwon.plus.ui.main.time_table.addTimeTable.addcell

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.suwon.plus.model.LectureModel

class AddTimeTableCellSharedViewModel(): ViewModel() {
    val lectureEntityLiveData = MutableLiveData<LectureModel>()
}