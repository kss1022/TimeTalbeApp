package com.example.suwon_university_community.widget.adapter.listener

import com.example.suwon_university_community.model.TimeTableModel

interface TimeTableListAdapterListener : AdapterListener{
    fun changeTimeTable(timeTableModel: TimeTableModel)

    fun editTimeTable(timeTableModel: TimeTableModel)

    fun deleteTimeTable(timeTableModel: TimeTableModel)
}