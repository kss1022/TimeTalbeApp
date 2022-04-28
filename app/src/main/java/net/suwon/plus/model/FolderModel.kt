package net.suwon.plus.model

import net.suwon.plus.data.entity.memo.FolderCategory


data class FolderModel(
    override val id: Long,
    override val type: CellType = CellType.FOLDER_CELL,
    val name : String,
    val count : Int = 0,
    val category : FolderCategory,
    val isDefault : Boolean = false,
    val timeTableId : Long? = null
) : Model( id , type)