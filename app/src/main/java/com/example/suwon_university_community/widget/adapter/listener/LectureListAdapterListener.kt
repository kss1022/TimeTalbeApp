package com.example.suwon_university_community.widget.adapter.listener

import com.example.suwon_university_community.model.LectureModel

interface LectureListAdapterListener : AdapterListener{
    fun selectLecture( model: LectureModel)
}