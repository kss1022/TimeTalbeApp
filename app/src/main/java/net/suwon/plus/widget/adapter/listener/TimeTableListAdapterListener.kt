package net.suwon.plus.widget.adapter.listener

import net.suwon.plus.model.TimeTableModel

interface TimeTableListAdapterListener : AdapterListener{
    fun changeTimeTable(timeTableModel: TimeTableModel)

    fun editTimeTable(timeTableModel: TimeTableModel)

    fun deleteTimeTable(timeTableModel: TimeTableModel)
}