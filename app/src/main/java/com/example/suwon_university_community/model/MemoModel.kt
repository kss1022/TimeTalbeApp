package com.example.suwon_university_community.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MemoModel (
    override val id: Long,
    override val type: CellType = CellType.MEMO_CELL,
    val title : String ="",
    val memo : String ="",
    val time : Long = 0L,
    val memoFolderId : Long,
    val timeTableCellId : Long? = null
) : Model(id , type) , Parcelable