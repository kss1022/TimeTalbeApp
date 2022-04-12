package com.example.suwon_university_community.model

import com.example.suwon_university_community.data.entity.memo.FolderCategory

data class FolderModel(
    override val id: Long,
    override val type: CellType = CellType.FOLDER_CELL,
    val name : String,
    val count : Int = 0,
    val category : FolderCategory,
    val isDefault : Boolean = false
) : Model( id , type)