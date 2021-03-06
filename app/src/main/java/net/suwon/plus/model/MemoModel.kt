package net.suwon.plus.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.suwon.plus.data.entity.memo.MemoImage


@Parcelize
data class MemoModel (
    override val id: Long,
    override val type: CellType = CellType.MEMO_CELL,
    val title : String ="",
    val memo : String ="",
    val time : Long = 0L,
    val memoFolderId : Long,
    val timeTableCellId : Long? = null,
    val imageUrlList : List<MemoImage> = listOf()
) : Model(id , type) , Parcelable