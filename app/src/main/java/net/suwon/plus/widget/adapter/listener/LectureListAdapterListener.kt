package net.suwon.plus.widget.adapter.listener

import net.suwon.plus.model.LectureModel

interface LectureListAdapterListener : AdapterListener{
    fun selectLecture( model: LectureModel)
}