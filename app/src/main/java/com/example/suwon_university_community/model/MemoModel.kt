package com.example.suwon_university_community.model


data class MemoModel (
    override val id: Long,
    override val type: CellType = CellType.MEMO_CELL,
    val title : String ="",
    val memo : String ="",
    val time : String ="",
    val memoFolderId : Long,
    val timeTableCellId : Long? = null
) : Model(id , type)