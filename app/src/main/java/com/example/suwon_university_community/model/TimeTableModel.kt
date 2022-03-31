package com.example.suwon_university_community.model

data class TimeTableModel(
    override val id: Long,
    override val type: CellType,
    val tableName : String,
    val year : Int,
    val semester : Int,
    val isDefault: Boolean
) : Model(id, type)